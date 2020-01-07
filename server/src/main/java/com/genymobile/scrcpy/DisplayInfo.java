package com.genymobile.scrcpy;

public final class DisplayInfo {
    private final Size size;
    private final int rotation;
    private final int type;
    private String name;
    private final int ownerUid;
    private String ownerPackageName;

    public DisplayInfo(Size size, int rotation, int type, String name, int ownerUid, String ownerPackageName) {
        this.size = size;
        this.rotation = rotation;
        this.type = type;
        this.name = name;
        this.ownerUid = ownerUid;
        this.ownerPackageName = ownerPackageName;
    }

    public Size getSize() {
        return size;
    }

    public int getRotation() {
        return rotation;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getOwnerPackageName() {
        return ownerPackageName;
    }

    public int getOwnerUid() {
        return ownerUid;
    }
}

