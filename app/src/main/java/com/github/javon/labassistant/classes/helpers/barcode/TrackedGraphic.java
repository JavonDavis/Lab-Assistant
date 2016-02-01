package com.github.javon.labassistant.classes.helpers.barcode;

/**
 * Common base class for defining graphics for a particular item type.  This along with
 * {@link GraphicTracker} avoids the need to duplicate this code for both the face and barcode
 * instances.
 */
abstract class TrackedGraphic<T> extends GraphicOverlay.Graphic {
    private int mId;

    TrackedGraphic(GraphicOverlay overlay) {
        super(overlay);
    }

    void setId(int id) {
        mId = id;
    }

    protected int getId() {
        return mId;
    }

    abstract void updateItem(T item);
}
