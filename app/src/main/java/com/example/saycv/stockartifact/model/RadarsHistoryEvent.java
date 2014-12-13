package com.example.saycv.stockartifact.model;

import org.saycv.sgs.media.SgsMediaType;
import org.saycv.sgs.model.SgsHistoryEvent;
import org.saycv.sgs.utils.SgsPredicate;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
public class RadarsHistoryEvent extends SgsHistoryEvent {
    @Element(data=true, required=false)
    protected String mContent;

    protected List<Radar> mRadarsData;

    public RadarsHistoryEvent(){
        super(SgsMediaType.Radars, null);
    }

    public RadarsHistoryEvent(String remoteParty, String displayName) {
        super(SgsMediaType.Radars, remoteParty);
        super.setDisplayName(displayName);
    }

    public void setContent(String content){
        this.mContent = content;
    }

    public String getContent(){
        return this.mContent;
    }

    public void setContent(List<Radar> content){
        this.mContent = new String();
        this.mRadarsData = content;

        if ( content!=null ) {
            for (Radar i : content) {
                this.mContent += i.toString();
            }
        }
    }

    public List<Radar> getRadars(){
        return this.mRadarsData;
    }

    /**
     * RadarsHistoryEventFilter
     */
    public static class RadarsHistoryEventFilter implements SgsPredicate<SgsHistoryEvent>{
        @Override
        public boolean apply(SgsHistoryEvent event) {
            return (event != null && (event.getMediaType() == SgsMediaType.Radars));
        }
    }
}
