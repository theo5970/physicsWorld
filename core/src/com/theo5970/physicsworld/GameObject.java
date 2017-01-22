package com.theo5970.physicsworld;

import com.badlogic.gdx.utils.ObjectMap;
import com.theo5970.physicsworld.component.ColorComponent;
import com.theo5970.physicsworld.component.Component;
import com.theo5970.physicsworld.component.ComponentType;
import com.theo5970.physicsworld.component.TransformComponent;

public class GameObject implements IDisposable {
    private boolean disposed;
    private static int count;
    private long id;
    private Component[] components;

    public GameObject() {
        this.components = new Component[64];
        count++;
        id = count;
    }

    public void addComponent(Component component) {
        ComponentType type = ComponentType.getComponentType(component.getClass());
        int index = type.getIndex();
        components[index] = component;
    }

    public void addComponent(Component... components) {
        for (Component component : components) {
            addComponent(component);
        }
    }

    public boolean hasComponent(Class<? extends Component> componentType) {
        ComponentType type = ComponentType.getComponentType(componentType);
        int index = type.getIndex();
        return components[index] != null;
    }
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentType) {
        ComponentType type = ComponentType.getComponentType(componentType);
        return (T) components[type.getIndex()];
    }


    @Override
    public void dispose () {
        for (Component component : components) {
            if (component != null) {
                if (component instanceof IDisposable) {
                    ((IDisposable) component).dispose();
                }
            }
        }
        components = null;
        disposed = true;
    }

    public long getId () {
        return id;
    }

    public static void minusCount() {
        count--;
    }

    public boolean isDisposed () {
        return disposed;
    }
}
