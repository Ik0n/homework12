import java.util.ArrayList;

/**
 * Задание 2
 */
public class Sample02 {

    /**
     * Точка входа в программу
     * @param args
     */
    public static void main(String[] args) {
        // Можно указать своё значение потоков
        Resource resource = new Resource(8);

    }



}

/**
 * Класс с главным массивом и логикой его дробления на части для потоков и склейки обратно в единный массив
 */
class Resource {
    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;
    Float[] array = new Float[SIZE];
    long a;
    int numberThreads;
    int sizeForHalfArray;
    ArrayList<CommonResourceV2> arrayList;

    Resource() {

    }

    Resource(int numberThreads) {
        this.numberThreads = numberThreads;
        this.sizeForHalfArray = SIZE / numberThreads;

        for (int i = 0; i < array.length; i++) {
            array[i] = (float)1;
        }

        this.a = System.currentTimeMillis();

        splitArray();

        for (int i = 0; i < numberThreads; i++) {
            Thread thread = new MyThreadV2("Поток #" + (i + 1), this.arrayList.get(i));
            thread.start();
        }
        try {
            Thread.currentThread().sleep(5000);
            joinArrays();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * Метод разделения массива на подмассивы
     */
    public void splitArray() {

        this.arrayList = new ArrayList<>();
        ArrayList<Float[]> floats = new ArrayList<>();


        for (int i = 0; i < numberThreads; i++) {
            floats.add(new Float[sizeForHalfArray]);
        }

        for (int i = 0, s = sizeForHalfArray; i < floats.size(); i++, s += sizeForHalfArray) {

            Float[] buf = floats.get(i);
            System.arraycopy(this.array, s - sizeForHalfArray, buf, 0, sizeForHalfArray);
            floats.set(i, buf);

            System.out.println(System.currentTimeMillis() - a);

        }


        for (int i = 0; i < numberThreads; i++) {

            arrayList.add(new CommonResourceV2(floats.get(i)));

        }

    }

    /**
     * Метод объеденения массивов
     */
    public void joinArrays() {

        for (int i = 0, s = sizeForHalfArray; i < arrayList.size(); i++, s += sizeForHalfArray ) {

            System.arraycopy(arrayList.get(i).array, 0, this.array, s - sizeForHalfArray, sizeForHalfArray);

        }

        System.out.println(System.currentTimeMillis() - a - 5000);
    }


}

/**
 * Класс с логикой вычеслений внутри подмассивов
 */
class CommonResourceV2 extends Resource{

    Float[] array;
    long a = System.currentTimeMillis();

    CommonResourceV2() {

    }

    CommonResourceV2(Float[] array) {
        this.array = array;
    }

    /**
     * Вычесления для элементов массива
     */
    public void arrayComputing() {

        for (int i = 0; i < this.array.length; i++) {

            this.array[i] = (float)(this.array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

        }

        System.out.println(System.currentTimeMillis() - this.a);

    }


}

/**
 * Класс потока
 */
class MyThreadV2 extends Thread {

    CommonResourceV2 resource;

    MyThreadV2(String name, CommonResourceV2 resource) {
        super(name);
        this.resource = resource;
    }

    @Override
    public void run() {
        resource.arrayComputing();


    }
}


