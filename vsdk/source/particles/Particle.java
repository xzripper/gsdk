package vsdk.source.particles;

// Particle structure.
class Particle {
    private float[] pos;

    private int[] color;

    private float alpha;

    private float size;

    private float rotation;

    private int id;

    private boolean spawningProcess;

    private float lifetime;

    private float[] designatedVelocity;

    protected Particle(float[] pos_, int[] color_, float alpha_, float size_, float rotation_, float[] designatedVelocity_, int id_) {
        pos = pos_;
        color = color_;
        alpha = alpha_;
        size = size_;
        rotation = rotation_;

        designatedVelocity = designatedVelocity_;

        id = id_;

        spawningProcess = true;

        lifetime = 0;
    }

    protected void setPos(float[] pos_) {
        pos = pos_;
    }

    protected void addPos(float x, float y, float z) {
        setPos(new float[] {pos[0] + x, pos[1] + y, pos[2] + z});
    }

    protected void subPos(float x, float y, float z) {
        addPos(-x, -y, -z);
    }

    protected float[] getPos() {
        return pos;
    }

    protected void setColor(int[] color_) {
        color = color_;
    }

    protected void addColor(int r, int g, int b) {
        setColor(new int[] {color[0] + r, color[1] + g, color[2] + b});
    }

    protected void subColor(int r, int g, int b) {
        addColor(-r, -g, -b);
    }

    protected int[] getColor() {
        return color;
    }

    protected void setAlpha(float alpha_) {
        alpha = alpha_;
    }

    protected void addAlpha(float alpha_) {
        setAlpha(alpha + alpha_);
    }

    protected void subAlpha(float alpha_) {
        addAlpha(-alpha_);
    }

    protected float getAlpha() {
        return alpha;
    }

    protected void setSize(float size_) {
        size = size_;
    }

    protected void addSize(float size_) {
        setSize(size + size_);
    }

    protected void subSize(float size_) {
        addSize(-size_);
    }

    protected float getSize() {
        return size;
    }

    protected void setRotation(float rotation_) {
        rotation = rotation_;
    }

    protected void addRotation(float rotation_) {
        setRotation(rotation + rotation_);
    }

    protected float getRotation() {
        return rotation;
    }

    protected int getID() {
        return id;
    }

    protected void setSpawningProcess(boolean spawning) {
        spawningProcess = spawning;
    }

    protected boolean getSpawningProcess() {
        return spawningProcess;
    }

    protected void setLifetime(float lifetime_) {
        lifetime = lifetime_;
    }

    protected void subLifetime(float delta) {
        lifetime -= delta;
    }

    protected float getLifetime() {
        return lifetime;
    }

    protected boolean isDead() {
        return lifetime <= 0;
    }

    protected float[] getDesignatedVelocity() {
        return designatedVelocity;
    }
}
