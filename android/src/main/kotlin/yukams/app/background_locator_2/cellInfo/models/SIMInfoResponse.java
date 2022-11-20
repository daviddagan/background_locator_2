package yukams.app.background_locator_2.models;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Keep
public class SIMInfoResponse implements Serializable {

    private List<SIMInfo> simInfoList = new ArrayList<>();

    public SIMInfoResponse() {
    }

    public SIMInfoResponse(List<SIMInfo> simInfoList) {
        this.simInfoList = simInfoList;
    }

    public List<SIMInfo> getSimInfoList() {
        return simInfoList;
    }

    public void setSimInfoList(List<SIMInfo> simInfoList) {
        this.simInfoList = simInfoList;
    }
}
