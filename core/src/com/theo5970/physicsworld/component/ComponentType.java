package com.theo5970.physicsworld.component;

import com.badlogic.gdx.utils.ObjectMap;

public class ComponentType {
    private static ObjectMap<Class<? extends Component>, ComponentType> componentMap;
    private static int count = 0;
    private int index = 0;
    static {
        componentMap = new ObjectMap<Class<? extends Component>, ComponentType>();
        register(BodyComponent.class);
        register(TransformComponent.class);
        register(ColorComponent.class);
        register(TextureComponent.class);
        register(PlayerComponent.class);
        register(LifeComponent.class);
        register(BulletComponent.class);
        register(DamageComponent.class);
        register(LiftComponent.class);
        register(SpringComponent.class);
        register(ItemComponent.class);
        register(BitmapFontComponent.class);
        register(SmoothMoveComponent.class);
        register(ParticleComponent.class);
        register(FollowComponent.class);
    }
    private ComponentType () {
        count++;
        index = count;
    }

    public int getIndex() {
        return index;
    }

    private static void register(Class<? extends Component> classType) {
        if (!componentMap.containsKey(classType)) {
            componentMap.put(classType, new ComponentType());
        }
    }

    public static ComponentType getComponentType(Class<? extends Component> classType) {
        return componentMap.get(classType);
    }
}
