package com.calm.tools.botfy.domain;

import com.calm.tools.botfy.domain.common.Period;

import java.util.List;

public abstract class SingleDay {
    private int hours = 24;

    protected abstract List<Period> breaks();

    protected List<Period> availableTimes() {
        return null;
    }

    public List<OneThing> planned() {
        return null;
    }

    public List<OneThing> suggestion() {
        return null;
    }

    public List<OneThing> completed() {
        return null;
    }
}
