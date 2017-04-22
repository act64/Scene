package com.recovery.netwrok.model;

import java.util.List;

/**
 * Created by tom on 2017/4/22.
 */

public class UnitsInfo {

    private List<UnitsBean> units;

    public List<UnitsBean> getUnits() {
        return units;
    }

    public void setUnits(List<UnitsBean> units) {
        this.units = units;
    }

    public static class UnitsBean {
        /**
         * id : ks66DS66
         * name : 果蔬单元
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
