package yukams.app.background_locator_2.models.nr;

import androidx.annotation.Keep;

import yukams.app.background_locator_2.models.common.Band;

import java.io.Serializable;
@Keep
public class BandNR extends Band implements Serializable {

    private int downlinkArfcn;
    private int downlinkFrequency;

    public BandNR() {
    }

    public int getDownlinkArfcn() {
        return downlinkArfcn;
    }

    public void setDownlinkArfcn(int downlinkArfcn) {
        this.downlinkArfcn = downlinkArfcn;
    }

    public int getDownlinkFrequency() {
        return downlinkFrequency;
    }

    public void setDownlinkFrequency(int downlinkFrequency) {
        this.downlinkFrequency = downlinkFrequency;
    }
}
