package com.example.saycv.stockartifact.model;

import org.saycv.sgs.media.SgsMediaType;
import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsPredicate;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class HistoryRadarsEvent extends SgsHistoryEvent {

    @Element(data = true, required = false)
    protected String mContent;
    @Element(name = "TotalUpload", required = true)
    protected String mTotalUpload;
    @Element(name = "RadarsData", required = true)
    protected List<Radar> mRadarsData;
    //@Element(name = "RealFirstTimeChecked", required = true)
    //protected String mRealFirstTimeChecked;

    HistoryRadarsEvent() {
        this(null, SgsHistoryEvent.StatusType.Failed);
    }

    public HistoryRadarsEvent(String remoteParty, StatusType status) {
        super(SgsMediaType.Radars, remoteParty);
        super.setStatus(status);
    }

    public String getContent() {
        return this.mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getTotalUpload() {
        return this.mTotalUpload;
    }

    public void setTotalUpload(String content) {
        this.mTotalUpload = content;
    }

    public List<Radar> getRadarsData() {
        return this.mRadarsData;
    }

    public void setRadarsData(List<Radar> content) {
        this.mRadarsData = content;
    }

    //public void setRealFirstTimeChecked(String content) { this.mRealFirstTimeChecked = content; }
    //public String getRealFirstTimeChecked() { return this.mRealFirstTimeChecked; }

    public static class HistoryEventTrafficCountIntelligentFilter implements SgsPredicate<SgsHistoryEvent> {
        private final List<String> mRemoteParties = new ArrayList<String>();

        protected void reset() {
            mRemoteParties.clear();
        }

        @Override
        public boolean apply(SgsHistoryEvent event) {
            if (event != null && (event.getMediaType() == SgsMediaType.Radars)) {
                if (!mRemoteParties.contains(event.getRemoteParty())) {
                    mRemoteParties.add(event.getRemoteParty());
                    return true;
                }
            }
            return false;
        }
    }

    public static class HistoryEventTrafficCountFilter implements SgsPredicate<SgsHistoryEvent> {
        @Override
        public boolean apply(SgsHistoryEvent event) {
            return (event != null && (event.getMediaType() == SgsMediaType.Radars));
        }
    }
}
