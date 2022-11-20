package yukams.app.background_locator_2.models.cdma;


import androidx.annotation.Keep;

import yukams.app.background_locator_2.models.common.Band;
import yukams.app.background_locator_2.models.common.Cell;
import yukams.app.background_locator_2.models.nr.BandNR;
import yukams.app.background_locator_2.models.nr.SignalNR;

import java.io.Serializable;

@Keep
public class CellCDMA extends Cell implements Serializable {

    private Integer sid;
    private Integer nid;
    private Integer bid;
    private Double lat;
    private Double lon;
    private Band band;
    private SignalCDMA signalCDMA;


    public CellCDMA() {
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public SignalCDMA getSignalCDMA() {
        return signalCDMA;
    }

    public void setSignalCDMA(SignalCDMA signalCDMA) {
        this.signalCDMA = signalCDMA;
    }
}

