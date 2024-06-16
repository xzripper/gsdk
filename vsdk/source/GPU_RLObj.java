package vsdk.source;

class GPU_RLObj<RLObj> {
    public final RLObj rlObj;

    public final int gpuObjType;

    GPU_RLObj(RLObj rlObj_, int gpuObjType_) {
        rlObj = rlObj_;

        gpuObjType = gpuObjType_;
    }
}
