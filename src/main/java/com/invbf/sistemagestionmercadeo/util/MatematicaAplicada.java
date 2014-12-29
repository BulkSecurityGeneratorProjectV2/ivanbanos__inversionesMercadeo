/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invbf.sistemagestionmercadeo.util;

import com.invbf.sistemagestionmercadeo.entity.Denominacion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ivan
 */
public class MatematicaAplicada {

    public static boolean sePuedeLleagar(Float num, Float... multiplos) {
        while (num > 0) {
            Float dividir = 0f;
            for (Float multiplo : multiplos) {
                if (num % multiplo == 0) {
                    return true;
                } else {
                    if (num > multiplo) {
                        dividir = multiplo;
                    }
                }
            }
            if (dividir == 0) {
                return false;
            } else {
                num -= dividir;
            }
        }
        return false;
    }

    public static List<DenoinacionCant> getBonosAsignadosDEnominacinesGrandes(List<Denominacion> denominaciones, Float cantidad) {
        for (int i = denominaciones.size(); i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (denominaciones.get(j).getValor() > denominaciones.get(j + 1).getValor()) {
                    Collections.swap(denominaciones, j + 1, j);
                }
            }

        }
        float restante = cantidad;
        List<DenoinacionCant> denoinacionCants = new ArrayList<DenoinacionCant>();
        int punto = denominaciones.size();
        while (restante > 0) {
            for (int i = punto; i >= 0; i--) {
                Denominacion get = denominaciones.get(i);
                if (get.getValor() < restante) {
                    denoinacionCants.add(new DenoinacionCant(get, (int) (restante / get.getValor())));
                    restante %= get.getValor();
                }
            }
            if(restante!=0){
                punto--;
                if(punto <0){
                    break;
                }
                restante += denoinacionCants.get(0).getDenomiancion().getValor();
                denoinacionCants.get(0).setCantidad(denoinacionCants.get(0).getCantidad()-1);
                if(denoinacionCants.get(0).getCantidad()==0){
                    denoinacionCants.remove(0);
                }
            }
            
        }
        return denoinacionCants;
    }

    public static List<DenoinacionCant> getBonosAsignadosDEnominacinesNormales(List<Denominacion> denominaciones, Float cantidad) {
        for (int i = denominaciones.size(); i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (denominaciones.get(j).getValor() > denominaciones.get(j + 1).getValor()) {
                    Collections.swap(denominaciones, j + 1, j);
                }
            }

        }
        float restante = cantidad;
        List<DenoinacionCant> denoinacionCants = new ArrayList<DenoinacionCant>();
        int punto = denominaciones.size()-1;
        
        while (restante > 0) {
            for (int i = punto; i >= 0; i--) {
                Denominacion get = denominaciones.get(i);
                if (get.getValor() < restante) {
                    denoinacionCants.add(new DenoinacionCant(get, (int) (restante / get.getValor())));
                    restante %= get.getValor();
                }
            }
            if(restante!=0){
                punto--;
                if(punto <0){
                    break;
                }
                restante += denoinacionCants.get(0).getDenomiancion().getValor();
                denoinacionCants.get(0).setCantidad(denoinacionCants.get(0).getCantidad()-1);
                if(denoinacionCants.get(0).getCantidad()==0){
                    denoinacionCants.remove(0);
                }
            }
            
        }
        return denoinacionCants;
    }
}