package com.konoca.frames;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import com.konoca.objs.Augment;
import com.konoca.objs.URLObj;

public abstract class FrameFuncs
{
    protected ArrayList<URLObj> urls;

    public Predicate<String> searchValidation = (s) -> false;
    public Predicate<String> postSearch = (s) -> false;

    public abstract Augment getAugment();
    public abstract void reload();

    public void setURLs(ArrayList<URLObj> urls)
    {
        this.urls = urls;
    }
    public ArrayList<URLObj> getURLs()
    {
        return this.urls;
    }
}
