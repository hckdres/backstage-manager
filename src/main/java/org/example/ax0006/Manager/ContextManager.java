package org.example.ax0006.Manager;

import org.example.ax0006.Repository.RolRepository;
import org.example.ax0006.Repository.UsuarioRepository;
import org.example.ax0006.Service.AutenticacionService;
import org.example.ax0006.Service.ProfileService;
import org.example.ax0006.Service.RolService;
import org.example.ax0006.db.H2;

public class ContextManager {

    private H2 h2;
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private AutenticacionService autenService;
    private ProfileService profileService;
    private RolService rolService;
    private SesionManager sesion;

    public ContextManager(
            H2 h2,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            AutenticacionService autenService,
            ProfileService profileService,
            RolService rolService,
            SesionManager sesion
    ) {
        this.h2 = h2;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.autenService = autenService;
        this.profileService = profileService;
        this.rolService = rolService;
        this.sesion = sesion;
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

    public AutenticacionService getAutenService() {
        return autenService;
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public RolService getRolService() {
        return rolService;
    }

    public SesionManager getSesion() {
        return sesion;
    }
}