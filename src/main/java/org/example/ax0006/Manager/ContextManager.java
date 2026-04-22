package org.example.ax0006.Manager;

import org.example.ax0006.Repository.ConciertoRepository;
import org.example.ax0006.Repository.HorarioRepository;
import org.example.ax0006.Repository.NominaRepository;
import org.example.ax0006.Repository.RolRepository;
import org.example.ax0006.Repository.UsuarioRepository;
import org.example.ax0006.Service.AutenticacionService;
import org.example.ax0006.Service.ConciertoService;
import org.example.ax0006.Service.NominaService;
import org.example.ax0006.Service.ProfileService;
import org.example.ax0006.Service.RolService;
import org.example.ax0006.Service.StaffService;
import org.example.ax0006.db.H2;

public class ContextManager {

    private final H2 h2;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final HorarioRepository horarioRepository;
    private final ConciertoRepository conciertoRepository;
    private final AutenticacionService autenService;
    private final ProfileService profileService;
    private final RolService rolService;
    private final ConciertoService conciertoService;
    private final SesionManager sesion;
    private final StaffService staffService;
    private final NominaRepository nominaRepository;
    private final NominaService nominaService;

    public ContextManager(
            H2 h2,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            HorarioRepository horarioRepository,
            ConciertoRepository conciertoRepository,
            AutenticacionService autenService,
            ProfileService profileService,
            RolService rolService,
            ConciertoService conciertoService,
            SesionManager sesion,
            StaffService staffService,
            NominaRepository nominaRepository,
            NominaService nominaService
    ) {
        this.h2 = h2;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.horarioRepository = horarioRepository;
        this.conciertoRepository = conciertoRepository;
        this.autenService = autenService;
        this.profileService = profileService;
        this.rolService = rolService;
        this.conciertoService = conciertoService;
        this.sesion = sesion;
        this.staffService = staffService;
        this.nominaRepository = nominaRepository;
        this.nominaService = nominaService;
    }

    public H2 getH2() {
        return h2;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public RolRepository getRolRepository() {
        return rolRepository;
    }

    public HorarioRepository getHorarioRepository() {
        return horarioRepository;
    }

    public ConciertoRepository getConciertoRepository() {
        return conciertoRepository;
    }

    public AutenticacionService getAutenService() {
        return autenService;
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public RolService getRolService() {
        return rolService;
    }

    public ConciertoService getConciertoService() {
        return conciertoService;
    }

    public SesionManager getSesion() {
        return sesion;
    }

    public StaffService getStaffService() {
        return staffService;
    }

    public NominaRepository getNominaRepository() {
        return nominaRepository;
    }

    public NominaService getNominaService() {
        return nominaService;
    }
}
