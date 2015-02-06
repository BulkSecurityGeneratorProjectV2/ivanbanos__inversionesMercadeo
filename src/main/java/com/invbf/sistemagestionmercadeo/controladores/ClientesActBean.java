/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invbf.sistemagestionmercadeo.controladores;

import com.invbf.sistemagestionmercadeo.entity.Atributo;
import com.invbf.sistemagestionmercadeo.entity.Casino;
import com.invbf.sistemagestionmercadeo.entity.Categoria;
import com.invbf.sistemagestionmercadeo.entity.Cliente;
import com.invbf.sistemagestionmercadeo.entity.Clienteatributo;
import com.invbf.sistemagestionmercadeo.entity.ClienteatributoPK;
import com.invbf.sistemagestionmercadeo.entity.Permiso;
import com.invbf.sistemagestionmercadeo.entity.Tipodocumento;
import com.invbf.sistemagestionmercadeo.entity.Tipojuego;
import com.invbf.sistemagestionmercadeo.util.FacesUtil;
import com.invbf.sistemagestionmercadeo.util.Notificador;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author ideacentre
 */
@ManagedBean
@ViewScoped
public class ClientesActBean {

    private List<Tipojuego> tiposjuegos;
    private List<Atributo> atributos;
    private Cliente elemento;
    private Cliente viejo;
    @ManagedProperty("#{sessionBean}")
    private SessionBean sessionBean;
    private DualListModel<Tipojuego> tiposJuegosTodos;
    private List<Casino> listacasinos;
    private List<Categoria> listacategorias;
    private List<Tipodocumento> tipoDocumentos;
    private String observaciones;

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    /**
     * Creates a new instance of AtributosSistemaViewBean
     */
    public ClientesActBean() {
    }

    @PostConstruct
    public void init() {
        sessionBean.checkUsuarioConectado();
        sessionBean.setActive("clientes");
        if (!sessionBean.perfilViewMatch("Clientes")) {
            try {
                System.out.println("No lo coje");
                sessionBean.Desconectar();
                FacesContext.getCurrentInstance().getExternalContext().redirect("InicioSession.xhtml");
            } catch (IOException ex) {
            }
        }

        if (sessionBean.getAttributes() == null || !sessionBean.getAttributes().containsKey("idCliente")) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("AdministradorAtributosMarketing.xhtml");
            } catch (IOException ex) {
            }
        }
        if ((Integer) sessionBean.getAttributes().get("idCliente") != 0) {
            elemento = sessionBean.marketingUserFacade.findCliente((Integer) sessionBean.getAttributes().get("idCliente"));
            viejo = sessionBean.marketingUserFacade.findCliente((Integer) sessionBean.getAttributes().get("idCliente"));
            if (elemento.getIdTipoDocumento() == null) {
                elemento.setIdTipoDocumento(new Tipodocumento(0));
            }
            tiposjuegos = sessionBean.marketingUserFacade.findAllTiposjuegos();
            for (Tipojuego tj : elemento.getTipojuegoList()) {
                if (tiposjuegos.contains(tj)) {
                    tiposjuegos.remove(tj);
                }
            }
            tiposJuegosTodos = new DualListModel<Tipojuego>(tiposjuegos, elemento.getTipojuegoList());
        } else {
            elemento = new Cliente();
            elemento.setIdCliente(0);
            elemento.setIdTipoDocumento(new Tipodocumento(0));
            elemento.setIdCategorias(new Categoria(0));
            elemento.setIdCasinoPreferencial(new Casino(0));
            elemento.setTipojuegoList(new ArrayList<Tipojuego>());
            elemento.setClienteatributoList(new ArrayList<Clienteatributo>());
            tiposjuegos = sessionBean.marketingUserFacade.findAllTiposjuegos();
            tiposJuegosTodos = new DualListModel<Tipojuego>(tiposjuegos, elemento.getTipojuegoList());
        }
        atributos = sessionBean.marketingUserFacade.findAllAtributos();
        for (Atributo a : atributos) {
            Clienteatributo clientesatributos = new Clienteatributo(elemento.getIdCliente(), a.getIdAtributo());
            if (!elemento.getClienteatributoList().contains(clientesatributos)) {
                clientesatributos.setClienteatributoPK(new ClienteatributoPK(elemento.getIdCliente(), a.getIdAtributo()));
                clientesatributos.setAtributo(a);
                clientesatributos.setCliente(elemento);
                elemento.getClienteatributoList().add(clientesatributos);
            }
        }
        listacasinos = sessionBean.marketingUserFacade.findAllCasinos();
        listacategorias = sessionBean.marketingUserFacade.findAllCategorias();
        tipoDocumentos = sessionBean.marketingUserFacade.findAllTipoDocumentos();
    }

    public Cliente getElemento() {
        return elemento;
    }

    public void setElemento(Cliente elemento) {
        this.elemento = elemento;
    }

    public List<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Atributo> atributos) {
        this.atributos = atributos;
    }

    public DualListModel<Tipojuego> getTiposJuegosTodos() {
        return tiposJuegosTodos;
    }

    public void setTiposJuegosTodos(DualListModel<Tipojuego> tiposJuegosTodos) {
        this.tiposJuegosTodos = tiposJuegosTodos;
    }

    public void guardar() {
        try {

            if (elemento.getIdentificacion() == null || elemento.getIdentificacion().equals("")) {
                elemento.setIdTipoDocumento(null);
            }
            if ((elemento.getIdTipoDocumento() == null)
                    && (elemento.getIdentificacion() != null && !elemento.getIdentificacion().equals(""))) {
                FacesUtil.addErrorMessage("No se puede guardar cliente", "Si tiene identificación debe seleccionar un tipo");
            }

            elemento.setTipojuegoList(tiposJuegosTodos.getTarget());
            List<Clienteatributo> clienteatributos = elemento.getClienteatributoList();
            elemento.setClienteatributoList(new ArrayList<Clienteatributo>());

            if (elemento.getIdCliente() == 0) {
                elemento.setIdCliente(null);
                sessionBean.marketingUserFacade.guardarClientes(elemento);
                FacesUtil.addInfoMessage("Cliente creado con exito!", "");
                sessionBean.registrarlog("actualizar", "Clientes", "Cliente creado: " + elemento.toString());
                FacesContext.getCurrentInstance().getExternalContext().redirect("clientes.xhtml");
            } else {

                if (!elemento.getNombres().equals(viejo.getNombres())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "nombres", elemento.getNombres(), elemento.getNombres(), viejo.getNombres(), viejo.getNombres(), observaciones));
                }
                if (!elemento.getApellidos().equals(viejo.getApellidos())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "apellidos", elemento.getApellidos(), elemento.getApellidos(), viejo.getApellidos(), viejo.getApellidos(), observaciones));
                }
                if (elemento.getIdCasinoPreferencial() != null && !elemento.getIdCasinoPreferencial().equals(viejo.getIdCasinoPreferencial())) {
                    elemento.setIdCasinoPreferencial(sessionBean.marketingUserFacade.findCasino(elemento.getIdCasinoPreferencial().getIdCasino()));
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "idCasinoPreferencial", elemento.getIdCasinoPreferencial().getIdCasino().toString(), elemento.getIdCasinoPreferencial().getNombre(), viejo.getIdCasinoPreferencial().getIdCasino().toString(), viejo.getIdCasinoPreferencial().getNombre(), observaciones));
                }
                if (elemento.getIdCategorias() != null && !elemento.getIdCategorias().equals(viejo.getIdCategorias())) {

                    elemento.setIdCategorias(sessionBean.marketingUserFacade.findCategoria(elemento.getIdCategorias().getIdCategorias()));
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "idCategorias", elemento.getIdCategorias().getIdCategorias().toString(), elemento.getIdCategorias().getNombre(), viejo.getIdCategorias().getIdCategorias().toString(), viejo.getIdCategorias().getNombre(), observaciones));
                }
                if (!elemento.getTelefono1().equals(viejo.getTelefono1())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "telefono1", elemento.getTelefono1(), elemento.getTelefono1(), viejo.getTelefono1(), viejo.getTelefono1(), observaciones));
                }
                if (!elemento.getTelefono2().equals(viejo.getTelefono2())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "telefono2", elemento.getTelefono2(), elemento.getTelefono2(), viejo.getTelefono2(), viejo.getTelefono2(), observaciones));
                }
                if (!elemento.getIdentificacion().equals(viejo.getIdentificacion())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "identificacion", elemento.getIdentificacion(), elemento.getIdentificacion(), viejo.getIdentificacion(), viejo.getIdentificacion(), observaciones));
                }
                if (!elemento.getCorreo().equals(viejo.getCorreo())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "correo", elemento.getCorreo(), elemento.getCorreo(), viejo.getCorreo(), viejo.getCorreo(), observaciones));
                }
                if (elemento.getCumpleanos() != null && !elemento.getCumpleanos().equals(viejo.getCumpleanos())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "cumpleanos", elemento.getCumpleanos().getTime() + "", elemento.getCumpleanos().getTime() + "", viejo.getCumpleanos().getTime() + "", viejo.getCumpleanos().getTime() + "", observaciones));
                }
                if (!elemento.getPais().equals(viejo.getPais())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "pais", elemento.getPais(), elemento.getPais(), viejo.getPais(), viejo.getPais(), observaciones));
                }
                if (!elemento.getDireccion().equals(viejo.getDireccion())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "direccion", elemento.getDireccion(), elemento.getDireccion(), viejo.getDireccion(), viejo.getDireccion(), observaciones));
                }
                if (!elemento.getCiudad().equals(viejo.getCiudad())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "ciudad", elemento.getCiudad(), elemento.getCiudad(), viejo.getCiudad(), viejo.getCiudad(), observaciones));
                }
                if (!elemento.getBonoFidelizacion().equals(viejo.getBonoFidelizacion())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "bonoFidelizacion", elemento.getBonoFidelizacion(), elemento.getBonoFidelizacion(), viejo.getBonoFidelizacion(), viejo.getBonoFidelizacion(), observaciones));
                }
                if (!elemento.getGenero().equals(viejo.getGenero())) {
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "genero", elemento.getGenero(), elemento.getGenero(), viejo.getGenero(), viejo.getGenero(), observaciones));
                }
                if (elemento.getIdTipoDocumento() != null && !elemento.getIdTipoDocumento().equals(viejo.getIdTipoDocumento())) {
                    elemento.setIdTipoDocumento(sessionBean.marketingUserFacade.findTipoDocumento(elemento.getIdTipoDocumento().getIdTipoDocumento()));
                    sessionBean.managerUserFacade.addPermiso(new Permiso("EDITAR", elemento.getIdCliente().toString(), "CLIENTE", "idTipoDocumento", elemento.getIdTipoDocumento().getIdTipoDocumento().toString(), elemento.getIdTipoDocumento().getNombre(), viejo.getIdTipoDocumento().getIdTipoDocumento().toString(), viejo.getIdTipoDocumento().getNombre(), observaciones));
                }

                Notificador.notificar(Notificador.SOLICITUD_CAMBIO_CLIENTE, 
                        "Se pidió un cambio en el cliente " + elemento.getNombres() + " " + elemento.getApellidos() + ". Favor revisar la pagina de cambios en usuario.", 
                        "Cambio en cliente", sessionBean.getUsuario().getUsuariodetalle().getCorreo());
                FacesContext.getCurrentInstance().getExternalContext().redirect("clientes.xhtml");
                FacesUtil.addInfoMessage("Actualización enviada", "Pendiente de autorización");
                sessionBean.registrarlog("actualizar", "Clientes", "Cliente enviado a actualización:" + elemento.toString());

            }

        } catch (IOException ex) {
        }
    }

    public List<Casino> getListacasinos() {
        return listacasinos;
    }

    public void setListacasinos(List<Casino> listacasinos) {
        this.listacasinos = listacasinos;
    }

    public List<Categoria> getListacategorias() {
        return listacategorias;
    }

    public void setListacategorias(List<Categoria> listacategorias) {
        this.listacategorias = listacategorias;
    }

    public List<Tipodocumento> getTipoDocumentos() {
        return tipoDocumentos;
    }

    public void setTipoDocumentos(List<Tipodocumento> tipoDocumentos) {
        this.tipoDocumentos = tipoDocumentos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
