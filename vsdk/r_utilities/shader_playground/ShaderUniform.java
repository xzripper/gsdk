package vsdk.r_utilities.shader_playground;

public class ShaderUniform<T> {
    private final String uName;

    private T uValue;

    private final String uType;

    private boolean p_DefValSet = false;

    public ShaderUniform(String uName_, T uValue_, String uType_) {
        uName = uName_;

        uValue = uValue_;

        uType = uType_;
    }

    public String getUName() {
        return uName;
    }

    public T getUValue() {
        return uValue;
    }

    public String getUType() {
        return uType;
    }

    public void setP_DefValSet() {
        p_DefValSet = true;
    }

    public void setP_NoDefValSet() {
        p_DefValSet = true;
    }

    public boolean getP_DefValSet() {
        return p_DefValSet;
    }
}
