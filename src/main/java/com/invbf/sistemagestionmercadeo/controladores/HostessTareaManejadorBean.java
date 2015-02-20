/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invbf.sistemagestionmercadeo.controladores;

import com.invbf.sistemagestionmercadeo.entity.Accion;
import com.invbf.sistemagestionmercadeo.entity.Listasclientestareas;
import com.invbf.sistemagestionmercadeo.entity.Tarea;
import com.invbf.sistemagestionmercadeo.exceptions.EventoSinClientesPorRevisarException;
import com.invbf.sistemagestionmercadeo.util.FacesUtil;
import com.invbf.sistemagestionmercadeo.util.LCTPojo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ideacentre
 */
@ManagedBean
@ViewScoped
public class HostessTareaManejadorBean implements Serializable {

    private Tarea elemento;
    @ManagedProperty("#{sessionBean}")
    private SessionBean sessionBean;
    private List<LCTPojo> clientesPojo;
    private boolean isComing;

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    /**
     * Creates a new instance of AtributosSistemaViewBean
     */
    public HostessTareaManejadorBean() {
    }

    @PostConstruct
    public void init() {
        sessionBean.checkUsuarioConectado();
        sessionBean.setActive("eventoshostess");
        
        if (!sessionBean.perfilViewMatch("ManejadorEventosHostess")) {
            try {
                sessionBean.Desconectar();
                FacesUtil.addErrorMessage("Session finalizada", "No tiene credenciales para ingresar a esa pantalla");
                FacesContext.getCurrentInstance().getExternalContext().redirect("InicioSession.xhtml");

            } catch (IOException ex) {

                System.out.println(ex);
            }
        }

        if (sessionBean.getAttributes("idTarea")==null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("tareasHostess.xhtml");
            } catch (IOException ex) {

                System.out.println(ex);
            }
        }
        elemento = sessionBean.marketingUserFacade.findTarea((Integer) sessionBean.getAttributes("idTarea"));
        if ((Boolean)sessionBean.getAttributes("isComingTarea")!=null) {
            sessionBean.removeAttribute("isComingTarea");
            try {
                List<Listasclientestareas> clientes = sessionBean.hostessFacade.findClienteTareaHostess((Integer) sessionBean.getAttributes("idTarea"));
                clientesPojo = new ArrayList<LCTPojo>();
                for (Iterator<Listasclientestareas> it = clientes.iterator(); it.hasNext();) {
                    Listasclientestareas listasclientestareas = it.next();
                    clientesPojo.add(new LCTPojo(listasclientestareas));
                }
            } catch (EventoSinClientesPorRevisarException ex) {
                System.out.println(ex);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public Tarea getElemento() {
        return elemento;
    }

    public void setElemento(Tarea elemento) {
        this.elemento = elemento;
    }

    public void guardar(Integer idCliente) {
        guardar:
        {
            int index = 0;
            for (int i = 0; i < clientesPojo.size(); i++) {
                if (clientesPojo.get(index).getCliente().getIdCliente() == idCliente
                        && clientesPojo.get(index).getTareas().getIdTarea() == (Integer) sessionBean.getAttributes("idTarea")) {
                    index = i;
                }
            }
            clientesPojo.indexOf(new LCTPojo((Integer) sessionBean.getAttributes("idTarea"), idCliente));

            LCTPojo l = clientesPojo.get(index);
            if (l.getAccion() == null || l.getAccion() == 0) {
                FacesUtil.addErrorMessage("No se puede guardar accion de cliente", "Debe seleccionar una Accion");
                break guardar;
            }

            l = clientesPojo.remove(index);
            Accion a = sessionBean.marketingUserFacade.findAccion(l.getAccion());

            sessionBean.hostessFacade.guardarLCE(l.getListaclientetareas(sessionBean.getUsuario(), a), a.getIdAccion());

            sessionBean.registrarlog(null, null, "Cambio en cliente " + l.getCliente().toString() + " sobre tarea " + elemento.getNombre());

            List<Listasclientestareas> clientes = new ArrayList<Listasclientestareas>();
            for (Iterator<LCTPojo> it = clientesPojo.iterator(); it.hasNext();) {
                LCTPojo lct = it.next();
                clientes.add(lct.getListaclientetareas(null, a));
            }
            try {
                Listasclientestareas nuevo = sessionBean.hostessFacade.nuevoLCE((Integer) sessionBean.getAttributes("idTarea"), clientes, l.getListaclientetareas(sessionBean.getUsuario(), a));
                clientesPojo.add(new LCTPojo(nuevo));
            } catch (EventoSinClientesPorRevisarException ex) {
                if (clientesPojo.isEmpty()) {
                    FacesUtil.addInfoMessage("Tarea Finalizada", "");
                }
            }
        }
    }

    public void nuevo(Integer idCliente) {
        guardar:
        {
            int index = 0;
            for (int i = 0; i < clientesPojo.size(); i++) {
                if (clientesPojo.get(index).getCliente().getIdCliente() == idCliente
                        && clientesPojo.get(index).getTareas().getIdTarea() == (Integer) sessionBean.getAttributes("idTarea")) {
                    index = i;
                }
            }
            LCTPojo l = clientesPojo.remove(index);
            Accion a = sessionBean.marketingUserFacade.findByNombreAccion("INICIAL");

            List<Listasclientestareas> clientes = new ArrayList<Listasclientestareas>();
            for (Iterator<LCTPojo> it = clientesPojo.iterator(); it.hasNext();) {
                LCTPojo lct = it.next();
                clientes.add(lct.getListaclientetareas(null, a));
            }
            try {
                Listasclientestareas nuevo = sessionBean.hostessFacade.nuevoLCE((Integer) sessionBean.getAttributes("idTarea"), clientes, l.getListaclientetareas(sessionBean.getUsuario(), a));
                clientesPojo.add(new LCTPojo(nuevo));
            } catch (EventoSinClientesPorRevisarException ex) {
                if (clientesPojo.isEmpty()) {
                    FacesUtil.addInfoMessage("Tarea Finalizada", "");
                }
            }
        }
    }

    public List<LCTPojo> getClientesPojo() {
        return clientesPojo;
    }

    public void setClientesPojo(List<LCTPojo> clientesPojo) {
        this.clientesPojo = clientesPojo;
    }
}
