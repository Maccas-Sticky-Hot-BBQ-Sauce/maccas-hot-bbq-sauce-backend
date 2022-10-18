package com.translink.api.repository.addOn;

import com.translink.api.config.format.model.SpecializedTime;
import com.translink.api.repository.model.Stop;
import com.translink.api.repository.model.embed.Days;

public interface StopRepositoryCustom {
    Stop findStopByStopIdFilterByDayAndTime(String stopId, Days days, SpecializedTime fromTime, SpecializedTime toTime);
}
