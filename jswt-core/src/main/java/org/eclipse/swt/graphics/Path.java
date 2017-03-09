package org.eclipse.swt.graphics;


import org.eclipse.swt.SWT;

public class Path extends Resource {

    byte[] types = new byte[8];
    int typeCount = 0;
    float[] points = new float[32];
    int pointCount = 0;

    public Path(Device device) {
        this.device = device;
    }

    void ensurePointsSpace(int size) {
        if (pointCount + size > points.length) {
            float[] newPoints = new float[Math.max(points.length * 2, pointCount + size)];
            System.arraycopy(points, 0, newPoints, 0, pointCount);
            points = newPoints;
        }
    }

    void ensureTypesSpace(int size) {
        if (typeCount + size > types.length) {
            byte[] newTypes = new byte[Math.max(types.length * 2, typeCount + size)];
            System.arraycopy(types, 0, newTypes, 0, typeCount);
            types = newTypes;
        }
    }

    private void addType(int type) {
        peer = null;
        ensureTypesSpace(1);
        types[typeCount++] = (byte) type;
    }

    private void addPoint(float x, float y) {
        ensurePointsSpace(2);
        points[pointCount++] = x;
        points[pointCount++] = y;
    }

    public void	addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
        System.out.println("FIXME: Path.addArc");
    }

    public void	addPath(Path path) {
        ensureTypesSpace(path.typeCount);
        System.arraycopy(path.types, 0, types, typeCount, path.typeCount);
        typeCount += path.typeCount;

        ensurePointsSpace(path.pointCount);
        System.arraycopy(path.points, 0, points, pointCount, path.pointCount);
        pointCount += path.pointCount;
    }

    public void	addRectangle(float x, float y, float width, float height) {
        moveTo(x, y);
        lineTo(x + width, y);
        lineTo(x + width, y + width);
        lineTo(x, y + width);
        close();
    }

    public void	addString(String string, float x, float y, Font font) {
        System.out.println("FIXME: Path.addString");
    }

    public void	close() {
        addType(SWT.PATH_CLOSE);
    }

    public boolean	contains(float x, float y, GC gc, boolean outline) {
        return false;
    }


    public void	cubicTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
        addType(SWT.PATH_CUBIC_TO);
        addPoint(cx1, cy1);
        addPoint(cx2, cy2);
        addPoint(x, y);
    }

    public void	getBounds(float[] bounds) {
        System.out.println("FIXME: Path.getBounds");
    }

    public void	getCurrentPoint(float[] point) {
        if (pointCount == 0) {
            point[0] = 0;
            point[1] = 0;
        } else {
            point[0] = points[pointCount - 2];
            point[1] = points[pointCount - 1];
        }
    }

    public PathData	getPathData() {
        PathData pathData = new PathData();
        pathData.points = new float[pointCount];
        pathData.types = new byte[typeCount];
        System.arraycopy(points, 0, pathData.points, 0, pointCount);
        System.arraycopy(types, 0, pathData.types, 0, typeCount);
        return pathData;
    }


    public void	lineTo(float x, float y) {
        addType(SWT.PATH_LINE_TO);
        addPoint(x, y);
    }

    public void	moveTo(float x, float y) {
        addType(SWT.PATH_MOVE_TO);
        addPoint(x, y);
    }

    public void	quadTo(float cx, float cy, float x, float y) {
        addType(SWT.PATH_QUAD_TO);
        addPoint(cx, cy);
        addPoint(x, y);
    }

}
