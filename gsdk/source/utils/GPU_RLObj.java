package vsdk.source.utils;

class GPU_RLObj<RLObj> {
    protected final RLObj rlObj;

    protected final int gpuObjType;

    protected GPU_RLObj(RLObj rlObj_, int gpuObjType_) {
        rlObj = rlObj_;

        gpuObjType = gpuObjType_;
    }
}
