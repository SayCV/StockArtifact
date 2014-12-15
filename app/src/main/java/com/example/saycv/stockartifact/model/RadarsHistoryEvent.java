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
    protected List<Radar> mRadarsContent;
    @Element(data=true, required=false)
    protected List<Index> mIndexesContent;

    //protected List<Radar> mRadarsData;

    public RadarsHistoryEvent(){
        super(SgsMediaType.Radars, null);
    }

    public RadarsHistoryEvent(String remoteParty, String displayName) {
        super(SgsMediaType.Radars, remoteParty);
        super.setDisplayName(displayName);
    }

    /*public void setContent(String content){
        this.mContent = content;
    }*/

    public List<Radar> getRadarContent(){
        return this.mRadarsContent;
    }
    public List<Index> getIndexesContent(){
        return this.mIndexesContent;
    }

    public void setRadarContent(List<Radar> content){
        this.mRadarsContent = content;

        /*if ( content!=null ) {
            for (Radar i : content) {
                this.mRadarsContent += i.toString();
            }
        }*/
    }

    public void setIndexesContent(List<Index> content){
        this.mIndexesContent = content;
        /*if ( content!=null ) {
            for (Index i : content) {
                this.mIndexesContent += i.toString();
            }
        }*/
    }

    /*public List<Radar> getRadars(){
        return this.mRadarsData;
    }*/

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
