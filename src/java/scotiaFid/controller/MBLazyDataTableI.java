/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package scotiaFid.controller;

import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Arturo Mellado
 */
public interface MBLazyDataTableI<T> {
    LazyDataModel<T> getLazyDataModel(T cbc);
}