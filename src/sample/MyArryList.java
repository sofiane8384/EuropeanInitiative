package sample;

import java.util.ArrayList;

public class MyArryList<E>extends ArrayList<E> {

    @Override
    public boolean add(E object) {
        // Do some action here
        return super.add(object);

    };

    @Override
    public void add(int index, E object) {
        super.add(index, object);
        // Do some action here
    };

    @Override
    public E remove(int index) {
        // Do some action here
        return super.remove(index);
    }
}
