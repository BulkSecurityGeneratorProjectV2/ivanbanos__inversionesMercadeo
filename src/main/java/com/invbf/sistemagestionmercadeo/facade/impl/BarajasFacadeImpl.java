/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invbf.sistemagestionmercadeo.facade.impl;

import com.invbf.sistemagestionmercadeo.dao.GestionBarajasDao;
import com.invbf.sistemagestionmercadeo.dto.ActaDestruccionDTO;
import com.invbf.sistemagestionmercadeo.dto.BarajasCantidad;
import com.invbf.sistemagestionmercadeo.dto.BarajasDTO;
import com.invbf.sistemagestionmercadeo.dto.CasinoDto;
import com.invbf.sistemagestionmercadeo.dto.InventarioBarajasDTO;
import com.invbf.sistemagestionmercadeo.dto.MaterialesDTO;
import com.invbf.sistemagestionmercadeo.dto.OrdenCompraBarajaDTO;
import com.invbf.sistemagestionmercadeo.dto.SolicitudBarajasDTO;
import com.invbf.sistemagestionmercadeo.entity.Actasdestruccionbarajas;
import com.invbf.sistemagestionmercadeo.entity.Barajas;
import com.invbf.sistemagestionmercadeo.entity.Bodega;
import com.invbf.sistemagestionmercadeo.entity.Casino;
import com.invbf.sistemagestionmercadeo.entity.Destruccionbarajasmaestro;
import com.invbf.sistemagestionmercadeo.entity.Inventarobarajas;
import com.invbf.sistemagestionmercadeo.entity.Materialesbarajas;
import com.invbf.sistemagestionmercadeo.entity.Ordencomprabaraja;
import com.invbf.sistemagestionmercadeo.entity.Ordencomprabarajadetalle;
import com.invbf.sistemagestionmercadeo.entity.Solicitudbarajadetalle;
import com.invbf.sistemagestionmercadeo.entity.Solicitudbarajas;
import com.invbf.sistemagestionmercadeo.entity.Usuario;
import com.invbf.sistemagestionmercadeo.facade.BarajasFacade;
import com.invbf.sistemagestionmercadeo.util.CasinoBoolean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan
 */
public class BarajasFacadeImpl implements BarajasFacade, Serializable {

    private BarajasDTO transformarBaraja(Barajas baraja) {
        return new BarajasDTO(baraja.getId(), baraja.getColor(), baraja.getMarca(), baraja.getValorpromedio(), transformarMaterial(baraja.getMaterial()));
    }

    private Barajas transformarBaraja(BarajasDTO baraja) {
        return new Barajas(baraja.getId(), baraja.getColor(), baraja.getMarca(), baraja.getValorpromedio(), transformarMaterial(baraja.getMaterial()));
    }

    private MaterialesDTO transformarMaterial(Materialesbarajas material) {
        return new MaterialesDTO(material.getId(), material.getNombre(), material.getDescripcion());
    }

    private Materialesbarajas transformarMaterial(MaterialesDTO material) {
        return new Materialesbarajas(material.getId(), material.getNombre(), material.getDescripcion());
    }

    private List<InventarioBarajasDTO> transformarBodegas(List<Bodega> bodegas) {
        List<InventarioBarajasDTO> bodegasdto = new ArrayList<InventarioBarajasDTO>();
        for (Bodega bodega : bodegas) {
            bodegasdto.add(transformarInventario(bodega));
        }
        return bodegasdto;
    }

    private List<InventarioBarajasDTO> transformarBodegasParaSol(List<Bodega> bodegas) {
        List<InventarioBarajasDTO> bodegasdto = new ArrayList<InventarioBarajasDTO>();
        for (Bodega bodega : bodegas) {
            bodegasdto.add(transformarInventarioParaSol(bodega));
        }
        return bodegasdto;
    }

    private List<InventarioBarajasDTO> transformarBodegasParaDes(List<Bodega> bodegas) {
        List<InventarioBarajasDTO> bodegasdto = new ArrayList<InventarioBarajasDTO>();
        for (Bodega bodega : bodegas) {
            bodegasdto.add(transformarInventarioParaDes(bodega));
        }
        return bodegasdto;
    }

    private InventarioBarajasDTO transformarInventario(Bodega bodega) {

        InventarioBarajasDTO bodegadto = new InventarioBarajasDTO();
        bodegadto.setId(bodega.getId());
        bodegadto.setNombre(bodega.getNombre());
        for (Inventarobarajas item : bodega.getInventarobarajasList()) {
            bodegadto.getInventario().add(new BarajasCantidad(item.getId(), transformarBaraja(item.getBaraja()), item.getCantidadbarajas(), item.getCantidadbarajas(), item.getUso(), item.getPordestruir(), item.getDestruidas(), item.getMax(), item.getMin(), bodegadto.getNombre()));
        }
        for (Casino casino : bodega.getCasinosList()) {
            bodegadto.getCasinos().add(transformarCasino(casino));
        }
        return bodegadto;
    }
    private InventarioBarajasDTO transformarInventarioO(Bodega bodega) {

        InventarioBarajasDTO bodegadto = new InventarioBarajasDTO();
        bodegadto.setId(bodega.getId());
        bodegadto.setNombre(bodega.getNombre());
        for (Inventarobarajas item : bodega.getInventarobarajasList()) {
            bodegadto.getInventario().add(new BarajasCantidad(item.getId(), transformarBaraja(item.getBaraja()), item.getCantidadbarajas(), item.getCantidadbarajas(), item.getUso(), item.getPordestruir(), item.getDestruidas(), item.getMax(), item.getMin(), bodegadto.getNombre()));
        }
        for (Casino casino : bodega.getCasinosList()) {
            bodegadto.getCasinos().add(transformarCasino(casino));
        }
        return bodegadto;
    }

    private InventarioBarajasDTO transformarInventarioParaSol(Bodega bodega) {

        InventarioBarajasDTO bodegadto = new InventarioBarajasDTO();
        bodegadto.setId(bodega.getId());
        bodegadto.setNombre(bodega.getNombre());
        for (Inventarobarajas item : bodega.getInventarobarajasList()) {
            bodegadto.getInventario().add(new BarajasCantidad(item.getId(), transformarBaraja(item.getBaraja()), 0, (item.getCantidadbarajas() - item.getDestruidas() - item.getPordestruir() - item.getUso()), 0, 0, 0, 0, 0, bodegadto.getNombre()));
        }
        for (Casino casino : bodega.getCasinosList()) {
            bodegadto.getCasinos().add(transformarCasino(casino));
        }
        return bodegadto;
    }

    private InventarioBarajasDTO transformarInventarioParaDes(Bodega bodega) {

        InventarioBarajasDTO bodegadto = new InventarioBarajasDTO();
        bodegadto.setId(bodega.getId());
        bodegadto.setNombre(bodega.getNombre());
        for (Inventarobarajas item : bodega.getInventarobarajasList()) {
            bodegadto.getInventario().add(new BarajasCantidad(item.getId(), transformarBaraja(item.getBaraja()), 0, 0, 0, item.getPordestruir(), 0, 0, 0, bodegadto.getNombre()));
        }
        for (Casino casino : bodega.getCasinosList()) {
            bodegadto.getCasinos().add(transformarCasino(casino));
        }
        return bodegadto;
    }

    private CasinoDto transformarCasino(Casino casino) {
        return new CasinoDto(casino.getIdCasino(), casino.getNombre());
    }

    private List<OrdenCompraBarajaDTO> transformarOrdenesCompra(List<Ordencomprabaraja> listaOrdenesCompraBarajas) {
        List<OrdenCompraBarajaDTO> ordenes = new ArrayList<OrdenCompraBarajaDTO>();

        for (Ordencomprabaraja item : listaOrdenesCompraBarajas) {
            ordenes.add(transormarOrdenCompra(item));
        }

        return ordenes;
    }

    private List<SolicitudBarajasDTO> transformarSolicitudesBarajas(List<Solicitudbarajas> listaSoliciudesBarajas) {
        List<SolicitudBarajasDTO> ordenes = new ArrayList<SolicitudBarajasDTO>();

        for (Solicitudbarajas item : listaSoliciudesBarajas) {
            ordenes.add(transormarSolicitudBarajas(item));
        }

        return ordenes;
    }

    private OrdenCompraBarajaDTO transormarOrdenCompra(Ordencomprabaraja item) {
        OrdenCompraBarajaDTO orden = new OrdenCompraBarajaDTO();
        orden.setEstado(item.getEsatdo());
        orden.setId(item.getId());
        orden.setFechaAceptada(item.getFechaAceptada());
        orden.setFechaCreacion(item.getFechaCreacion());
        orden.setFechaRecibida(item.getFechaRecibida());
        orden.setUsuarioCreado(item.getCreador() == null ? "" : item.getCreador().getNombreUsuario());
        orden.setUsuarioAceptador(item.getAceptador() == null ? "" : item.getAceptador().getNombreUsuario());
        orden.setUsuarioREcibidor(item.getRecibidor() == null ? "" : item.getRecibidor().getNombreUsuario());
        for (Ordencomprabarajadetalle detalle : item.getOrdencomprabarajadetalleList()) {
            orden.getCantidades().add(new BarajasCantidad(detalle.getInventarobarajas().getId(), transformarBaraja(detalle.getInventarobarajas().getBaraja()), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getInventarobarajas().getBodega().getNombre()));
        }
        return orden;
    }

    private SolicitudBarajasDTO transormarSolicitudBarajas(Solicitudbarajas item) {
        SolicitudBarajasDTO orden = new SolicitudBarajasDTO();
        orden.setEstado(item.getEstado());
        orden.setId(item.getId());
        orden.setFechaAceptada(item.getFechentrega());
        orden.setFechaCreacion(item.getFechacreacion());
        orden.setFechaRecibida(item.getFecharecepcion());
        orden.setFechaDestruccion(item.getFechaDestruccion());
        orden.setEntregadasnuevas(item.getEntregadasNuevas());
        orden.setEntregadasusadas(item.getEntregadasUsadas());
        orden.setRecibidasnuevas(item.getRecibidasNuevas());
        orden.setRecibidasusadas(item.getRecibidasUsadas());
        orden.setUsuarioCreado(item.getCreador() == null ? "" : item.getCreador().getNombreUsuario());
        orden.setUsuarioAceptador(item.getAceptador() == null ? "" : item.getAceptador().getNombreUsuario());
        orden.setUsuarioREcibidor(item.getRecibidor() == null ? "" : item.getRecibidor().getNombreUsuario());
        orden.setUsuarioDestructor(item.getDestructor() == null ? "" : item.getDestructor().getNombreUsuario());
        for (Solicitudbarajadetalle detalle : item.getSolicitudbarajadetalleList()) {
            orden.getCantidades().add(new BarajasCantidad(detalle.getInventarobarajas().getId(), transformarBaraja(detalle.getInventarobarajas().getBaraja()), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getCantidad(), detalle.getInventarobarajas().getBodega().getNombre()));
        }
        return orden;
    }

    @Override
    public List<BarajasDTO> getListaBarajas() {
        List<Barajas> barajas = GestionBarajasDao.getListaBArajas();
        List<BarajasDTO> barajasDTO = new ArrayList<BarajasDTO>();
        for (Barajas baraja : barajas) {
            barajasDTO.add(transformarBaraja(baraja));
        }
        return barajasDTO;
    }

    @Override
    public List<MaterialesDTO> getListaMateriales() {
        List<Materialesbarajas> materiales = GestionBarajasDao.getListaMateriales();
        List<MaterialesDTO> materialesDTOs = new ArrayList<MaterialesDTO>();
        for (Materialesbarajas material : materiales) {
            System.out.println("Nombre" + material.getNombre());
            materialesDTOs.add(transformarMaterial(material));
        }
        return materialesDTOs;
    }

    @Override
    public MaterialesDTO addMaterial(MaterialesDTO material) {
        return transformarMaterial(GestionBarajasDao.addMaterial(transformarMaterial(material)));
    }

    @Override
    public MaterialesDTO deleteMaterial(MaterialesDTO material) {
        GestionBarajasDao.deleteMaterial(transformarMaterial(material));
        return material;
    }
    @Override
    public MaterialesDTO editMaterial(MaterialesDTO material) {
        return transformarMaterial(GestionBarajasDao.editMaterial(transformarMaterial(material)));
    }

    @Override
    public BarajasDTO addBaraja(BarajasDTO elemento) {
        elemento = transformarBaraja(GestionBarajasDao.addBaraja(transformarBaraja(elemento)));
        return elemento;
    }

    @Override
    public BarajasDTO deleteBaraja(BarajasDTO elemento) {
        GestionBarajasDao.deleteBaraja(transformarBaraja(elemento));
        return elemento;
    }

    @Override
    public InventarioBarajasDTO getInventario(Integer id) {
        return transformarInventario(GestionBarajasDao.getListaInvenratioBarajas(id));
    }

    @Override
    public List<OrdenCompraBarajaDTO> getOrdenesCompra() {
        return transformarOrdenesCompra(GestionBarajasDao.getListaOrdenesCompraBarajas());
    }

    @Override
    public int crearOrdenBarajas(List<InventarioBarajasDTO> inventario, Usuario usuario) {
        Ordencomprabaraja orden = new Ordencomprabaraja();
        orden.setFechaCreacion(new Date());
        orden.setCreador(usuario);
        orden.setEsatdo("GENERADA");
        orden = GestionBarajasDao.crearOrdenCompra(orden);
        for (InventarioBarajasDTO inventario1 : inventario) {
            orden.getOrdencomprabarajadetalleList().addAll(getDetallesOrden(inventario1, orden.getId()));
        }
        return GestionBarajasDao.guardarOrdenCompra(orden).getId();

    }

    private List<Ordencomprabarajadetalle> getDetallesOrden(InventarioBarajasDTO inventario, Integer id) {
        List<Ordencomprabarajadetalle> detalles = new ArrayList<Ordencomprabarajadetalle>();
        for (BarajasCantidad barajas : inventario.getInventario()) {
            detalles.add(getDettaleOrden(barajas, id));
        }
        return detalles;
    }

    private List<Solicitudbarajadetalle> getDetallesSolicitud(InventarioBarajasDTO inventario, Integer id) {
        List<Solicitudbarajadetalle> detalles = new ArrayList<Solicitudbarajadetalle>();
        for (BarajasCantidad barajas : inventario.getInventario()) {
            detalles.add(getDettaleSolicitud(barajas, id));
        }
        return detalles;
    }

    private Ordencomprabarajadetalle getDettaleOrden(BarajasCantidad barajas, Integer id) {
        Ordencomprabarajadetalle detalle = new Ordencomprabarajadetalle(id, barajas.getId());
        detalle.setCantidad(barajas.getCantidad());
        detalle.setCantidadAprobada(barajas.getCantidadR());
        return detalle;
    }

    private Solicitudbarajadetalle getDettaleSolicitud(BarajasCantidad barajas, Integer id) {
        Solicitudbarajadetalle detalle = new Solicitudbarajadetalle(id, barajas.getId());
        detalle.setCantidad(barajas.getCantidad());
        return detalle;
    }

    @Override
    public OrdenCompraBarajaDTO getOrden(Integer idOrden) {
        return transormarOrdenCompra(GestionBarajasDao.getOrdenCompraBaraja(idOrden));
    }

    @Override
    public void aprobarOrden(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.aprobarOrden(idOrden, usuario);
    }

    @Override
    public void recibirOrden(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.recibirOrden(idOrden, usuario);
    }

    @Override
    public List<SolicitudBarajasDTO> getSolicitudesBarajas(boolean getTodas, int idUsuario) {
        if (getTodas) {
            return transformarSolicitudesBarajas(GestionBarajasDao.getListaSoliciudesBarajas());
        } else {
            return transformarSolicitudesBarajas(GestionBarajasDao.getListaSoliciudesBarajas(idUsuario));
        }
    }

    @Override
    public int crearSolicitudBarajas(InventarioBarajasDTO inventario, Usuario usuario) {
        Solicitudbarajas orden = new Solicitudbarajas();
        orden.setFechacreacion(new Date());
        orden.setCreador(usuario);
        orden.setEstado("CREADA");
        orden = GestionBarajasDao.crearSolicitudBarajas(orden);
        orden.setSolicitudbarajadetalleList(getDetallesSolicitud(inventario, orden.getId()));
        return GestionBarajasDao.guardarSolicitudBarajas(orden).getId();
    }

    @Override
    public SolicitudBarajasDTO getSolicitud(Integer idOrden) {
        return transormarSolicitudBarajas(GestionBarajasDao.getSolicitudBaraja(idOrden));
    }

    @Override
    public void entregarNuevasSolicitud(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.entregarNuevasSolicitud(idOrden, usuario);

    }

    @Override
    public void recibirNuevasSolicitud(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.recibirNuevasSolicitud(idOrden, usuario);

    }

    @Override
    public void entregarUsadasSolicitud(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.entregarUsadasSolicitud(idOrden, usuario);
    }

    @Override
    public void recibirUsadasSolicitud(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.recibirUsadasSolicitud(idOrden, usuario);
    }

    @Override
    public List<InventarioBarajasDTO> getBodegas() {
        return transformarBodegas(GestionBarajasDao.getBodegas());
    }

    @Override
    public Integer crearBodega(String nombre) {
        return GestionBarajasDao.crearBodega(nombre);
    }

    @Override
    public void guardarBodega(InventarioBarajasDTO inventario, List<CasinoBoolean> casinos) {
        GestionBarajasDao.guardarBodega(inventario, casinos);
    }

    @Override
    public List<InventarioBarajasDTO> getBodegas(Usuario usuario) {
        return transformarBodegas(GestionBarajasDao.getBodegasUsusario(usuario));
    }

    @Override
    public List<InventarioBarajasDTO> getBodegasParaSol(Usuario usuario) {
        return transformarBodegasParaSol(GestionBarajasDao.getBodegasUsusario(usuario));
    }

    @Override
    public Date getFechaDestruccion(Integer idOrden) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ActaDestruccionDTO> getActasDestruccion() {
        return transformarActasDestruccion(GestionBarajasDao.getActasDestruccion());
    }

    private List<ActaDestruccionDTO> transformarActasDestruccion(List<Destruccionbarajasmaestro> actasDestruccion) {
        List<ActaDestruccionDTO> actas = new ArrayList<ActaDestruccionDTO>();
        for (Destruccionbarajasmaestro actasDestruccion1 : actasDestruccion) {

            actas.add(tranformarActaDestruccion(actasDestruccion1));

        }
        return actas;
    }

    private BarajasCantidad transformarDetalledestruccion(Actasdestruccionbarajas detalle) {
        BarajasCantidad detalled = new BarajasCantidad();
        detalled.setBaraja(transformarBaraja(detalle.getInventario().getBaraja()));
        detalled.setCantidad(detalle.getCantidad());
        return detalled;
    }

    @Override
    public ActaDestruccionDTO getBodegasParaDes(Usuario usuario) {
        ActaDestruccionDTO nueva = new ActaDestruccionDTO();
        nueva.setDetalle(transformarDetalleDestrucinoNuevo(GestionBarajasDao.getBodegas(usuario)));
        return nueva;
    }

    @Override
    public ActaDestruccionDTO getBodegasParaDesPorId(Usuario usuario, Integer idOrden) {
        return tranformarActaDestruccion(GestionBarajasDao.getDestruccionMaestro(usuario, idOrden));
    }

    @Override
    public Integer destruir(ActaDestruccionDTO acta, Usuario usuario) {
        return GestionBarajasDao.destruir(acta, usuario);
    }

    private ActaDestruccionDTO tranformarActaDestruccion(Destruccionbarajasmaestro actasDestruccion1) {
        ActaDestruccionDTO acta = new ActaDestruccionDTO();
        acta.setId(actasDestruccion1.getId());
        acta.setUsuario(actasDestruccion1.getUsuario().getNombreUsuario());
        acta.setFecha(actasDestruccion1.getFechaDestruccion());
        for (Actasdestruccionbarajas detalle : actasDestruccion1.getActasdestruccionbarajasList()) {
            acta.getDetalle().add(transformarDetalledestruccion(detalle));
        }
        return acta;
    }

    private List<BarajasCantidad> transformarDetalleDestrucinoNuevo(List<Bodega> bodegas) {
        List<BarajasCantidad> lista = new ArrayList<BarajasCantidad>();
        for (Bodega bodega : bodegas) {
            for (Inventarobarajas inventario : bodega.getInventarobarajasList()) {
                lista.add(new BarajasCantidad(inventario.getId(), transformarBaraja( inventario.getBaraja()), inventario.getPordestruir(), Integer.MIN_VALUE, Integer.SIZE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.SIZE, Integer.SIZE, null));
        
            }
        }
        return lista;
    }

    @Override
    public void crearOrden(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.crearOrden(idOrden, usuario);
    }

    @Override
    public OrdenCompraBarajaDTO getOrdenRecibir(Integer idOrden, Usuario usuario) {
        return transormarOrdenCompra(GestionBarajasDao.getOrdenCompraBaraja(idOrden, usuario));
    }

    @Override
    public void recibirOrdenCaja(Integer idOrden, Usuario usuario) {
        GestionBarajasDao.recibirOrdenCaja(idOrden, usuario);
    }

    @Override
    public List<OrdenCompraBarajaDTO> getOrdenesCompra(Usuario usuario) {
        return transformarOrdenesCompra(GestionBarajasDao.getListaOrdenesCompraBarajasUsuario(usuario));
    }


    

}
