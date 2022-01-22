
/**
 * 1 задание
 */
public class Sample01 {
    /**
     * Точка входа в программу
     * @param args
     */
    public static void main(String[] args) {

        CommonResource resource = new CommonResource();
        new MyThread("Поток 1", resource).start();

    }
}

/**
 * Класс с массивом
 */
class CommonResource {

    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;
    float[] array = new float[SIZE];
    long a;

    /**
     * Метод инициализации массива
     */
    public void arrayInit() {

        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }

        this.a = System.currentTimeMillis();
    }

    /**
     * Запуск вычеслений
     */
    public void arrayComputing() {

        for (int i = 0; i < array.length; i++) {

            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

        }

        System.out.println(System.currentTimeMillis() - a);

    }


}

/**
 * Класс потока
 */
class MyThread extends Thread {

    CommonResource resource;

    MyThread(String name, CommonResource resource) {
        super(name);
        this.resource = resource;
    }

    @Override
    public void run() {
        resource.arrayInit();
        resource.arrayComputing();
    }
}


