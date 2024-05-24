/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package scotiaFid.bean;

import java.io.Serializable;

/**
 *
 * @author lenovo
 */
    public class KeyValueBean implements Serializable {

        private String key;
        private String value;

        public KeyValueBean(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public KeyValueBean() {
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }