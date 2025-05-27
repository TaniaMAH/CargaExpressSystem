package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase que representa la información de la empresa de transporte.
 * Contiene datos corporativos, configuraciones y información de contacto.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class Empresa {
    
    // ==================== ATRIBUTOS ====================
    
    /**
     * Nombre comercial de la empresa
     */
    private String nombre;
    
    /**
     * Número de Identificación Tributaria (NIT)
     */
    private String nit;
    
    /**
     * Dirección física de la empresa
     */
    private String direccion;
    
    /**
     * Teléfono principal de contacto
     */
    private String telefono;
    
    /**
     * Correo electrónico corporativo
     */
    private String email;
    
    /**
     * Ruta del archivo del logo de la empresa
     */
    private String logo;
    
    /**
     * Página web de la empresa
     */
    private String sitioWeb;
    
    /**
     * Fecha de fundación de la empresa
     */
    private LocalDate fechaFundacion;
    
    /**
     * Razón social completa
     */
    private String razonSocial;
    
    /**
     * Representante legal
     */
    private String representanteLegal;
    
    /**
     * Teléfono secundario o móvil
     */
    private String telefonoSecundario;
    
    /**
     * Ciudad donde opera la empresa
     */
    private String ciudad;
    
    /**
     * Departamento/Estado donde opera
     */
    private String departamento;
    
    /**
     * País donde opera la empresa
     */
    private String pais;
    
    /**
     * Código postal
     */
    private String codigoPostal;
    
    /**
     * Descripción o misión de la empresa
     */
    private String descripcion;
    
    // ==================== PATRONES DE VALIDACIÓN ====================
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(Constants.EMAIL_PATTERN);
    private static final Pattern PHONE_PATTERN = Pattern.compile(Constants.PHONE_PATTERN);
    private static final Pattern NIT_PATTERN = Pattern.compile("^[0-9]{9}-[0-9]$");
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa la empresa con valores predeterminados.
     */
    public Empresa() {
        this.nombre = "Carga Express S.A.S";
        this.nit = "";
        this.direccion = "";
        this.telefono = "";
        this.email = "";
        this.logo = "";
        this.sitioWeb = "";
        this.fechaFundacion = LocalDate.now().minusYears(5);
        this.razonSocial = "Carga Express S.A.S";
        this.representanteLegal = "";
        this.telefonoSecundario = "";
        this.ciudad = "";
        this.departamento = "";
        this.pais = "Colombia";
        this.codigoPostal = "";
        this.descripcion = "Empresa de servicios de transporte y logística";
    }
    
    /**
     * Constructor básico con datos principales.
     * 
     * @param nombre nombre de la empresa
     * @param nit número de identificación tributaria
     * @param direccion dirección física
     * @param telefono teléfono de contacto
     * @param email correo electrónico
     * @throws ValidationException si los datos son inválidos
     */
    public Empresa(String nombre, String nit, String direccion, String telefono, 
                   String email) throws ValidationException {
        this(); // Llamar constructor por defecto para inicializar otros campos
        setNombre(nombre);
        setNit(nit);
        setDireccion(direccion);
        setTelefono(telefono);
        setEmail(email);
    }
    
    /**
     * Constructor completo con todos los datos.
     * 
     * @param nombre nombre de la empresa
     * @param nit número de identificación tributaria
     * @param direccion dirección física
     * @param telefono teléfono principal
     * @param email correo electrónico
     * @param logo ruta del logo
     * @param sitioWeb página web
     * @param fechaFundacion fecha de fundación
     * @param representanteLegal representante legal
     * @throws ValidationException si los datos son inválidos
     */
    public Empresa(String nombre, String nit, String direccion, String telefono, 
                   String email, String logo, String sitioWeb, LocalDate fechaFundacion,
                   String representanteLegal) throws ValidationException {
        this(nombre, nit, direccion, telefono, email);
        setLogo(logo);
        setSitioWeb(sitioWeb);
        setFechaFundacion(fechaFundacion);
        setRepresentanteLegal(representanteLegal);
    }
    
    // ==================== GETTERS Y SETTERS CON VALIDACIÓN ====================
    
    /**
     * Obtiene el nombre de la empresa.
     * 
     * @return nombre de la empresa
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre de la empresa con validación.
     * 
     * @param nombre nombre de la empresa
     * @throws ValidationException si el nombre es inválido
     */
    public void setNombre(String nombre) throws ValidationException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw ValidationException.campoRequerido("nombre de empresa");
        }
        
        String nombreLimpio = nombre.trim();
        if (nombreLimpio.length() < 3 || nombreLimpio.length() > 100) {
            throw ValidationException.longitudInvalida("nombre de empresa", nombreLimpio, 3, 100);
        }
        
        this.nombre = nombreLimpio;
        
        // Si no se ha establecido razón social, usar el nombre
        if (razonSocial == null || razonSocial.equals("Carga Express S.A.S")) {
            this.razonSocial = nombreLimpio;
        }
    }
    
    /**
     * Obtiene el NIT de la empresa.
     * 
     * @return NIT de la empresa
     */
    public String getNit() {
        return nit;
    }
    
    /**
     * Establece el NIT con validación.
     * 
     * @param nit número de identificación tributaria
     * @throws ValidationException si el NIT es inválido
     */
    public void setNit(String nit) throws ValidationException {
        if (nit == null || nit.trim().isEmpty()) {
            throw ValidationException.campoRequerido("NIT");
        }
        
        String nitLimpio = nit.trim();
        
        // Permitir diferentes formatos de NIT
        if (!nitLimpio.matches("^[0-9]{9}-[0-9]$") && 
            !nitLimpio.matches("^[0-9]{8,12}$")) {
            throw ValidationException.formatoInvalido("NIT", nitLimpio, 
                "123456789-0 o 123456789");
        }
        
        this.nit = nitLimpio;
    }
    
    /**
     * Obtiene la dirección de la empresa.
     * 
     * @return dirección de la empresa
     */
    public String getDireccion() {
        return direccion;
    }
    
    /**
     * Establece la dirección con validación.
     * 
     * @param direccion dirección de la empresa
     * @throws ValidationException si la dirección es inválida
     */
    public void setDireccion(String direccion) throws ValidationException {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw ValidationException.campoRequerido("dirección");
        }
        
        String direccionLimpia = direccion.trim();
        if (direccionLimpia.length() < 10 || direccionLimpia.length() > 200) {
            throw ValidationException.longitudInvalida("dirección", direccionLimpia, 10, 200);
        }
        
        this.direccion = direccionLimpia;
    }
    
    /**
     * Obtiene el teléfono principal.
     * 
     * @return teléfono de la empresa
     */
    public String getTelefono() {
        return telefono;
    }
    
    /**
     * Establece el teléfono con validación.
     * 
     * @param telefono teléfono de la empresa
     * @throws ValidationException si el teléfono es inválido
     */
    public void setTelefono(String telefono) throws ValidationException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw ValidationException.campoRequerido("teléfono");
        }
        
        String telefonoLimpio = telefono.trim();
        if (!PHONE_PATTERN.matcher(telefonoLimpio).matches()) {
            throw ValidationException.formatoInvalido("teléfono", telefonoLimpio, 
                "Solo números, espacios, guiones y signo +");
        }
        
        this.telefono = telefonoLimpio;
    }
    
    /**
     * Obtiene el email de la empresa.
     * 
     * @return email corporativo
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email con validación.
     * 
     * @param email correo electrónico corporativo
     * @throws ValidationException si el email es inválido
     */
    public void setEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw ValidationException.campoRequerido("email");
        }
        
        String emailLimpio = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(emailLimpio).matches()) {
            throw ValidationException.formatoInvalido("email", emailLimpio, 
                "formato: contacto@empresa.com");
        }
        
        this.email = emailLimpio;
    }
    
    /**
     * Obtiene la ruta del logo.
     * 
     * @return ruta del archivo de logo
     */
    public String getLogo() {
        return logo;
    }
    
    /**
     * Establece la ruta del logo.
     * 
     * @param logo ruta del archivo de logo
     */
    public void setLogo(String logo) {
        this.logo = (logo == null) ? "" : logo.trim();
    }
    
    /**
     * Obtiene el sitio web.
     * 
     * @return URL del sitio web
     */
    public String getSitioWeb() {
        return sitioWeb;
    }
    
    /**
     * Establece el sitio web con validación básica.
     * 
     * @param sitioWeb URL del sitio web
     * @throws ValidationException si la URL es inválida
     */
    public void setSitioWeb(String sitioWeb) throws ValidationException {
        if (sitioWeb == null || sitioWeb.trim().isEmpty()) {
            this.sitioWeb = "";
            return;
        }
        
        String urlLimpia = sitioWeb.trim().toLowerCase();
        
        // Validación básica de URL
        if (!urlLimpia.startsWith("http://") && !urlLimpia.startsWith("https://")) {
            urlLimpia = "https://" + urlLimpia;
        }
        
        if (!urlLimpia.matches("^https?://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$")) {
            throw ValidationException.formatoInvalido("sitio web", sitioWeb, 
                "URL válida (ej: www.empresa.com)");
        }
        
        this.sitioWeb = urlLimpia;
    }
    
    /**
     * Obtiene la fecha de fundación.
     * 
     * @return fecha de fundación de la empresa
     */
    public LocalDate getFechaFundacion() {
        return fechaFundacion;
    }
    
    /**
     * Establece la fecha de fundación con validación.
     * 
     * @param fechaFundacion fecha de fundación
     * @throws ValidationException si la fecha es inválida
     */
    public void setFechaFundacion(LocalDate fechaFundacion) throws ValidationException {
        if (fechaFundacion == null) {
            this.fechaFundacion = LocalDate.now().minusYears(1);
            return;
        }
        
        if (fechaFundacion.isAfter(LocalDate.now())) {
            throw new ValidationException("fecha de fundación", fechaFundacion.toString(), 
                "No puede ser fecha futura");
        }
        
        if (fechaFundacion.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new ValidationException("fecha de fundación", fechaFundacion.toString(), 
                "Fecha demasiado antigua (antes de 1900)");
        }
        
        this.fechaFundacion = fechaFundacion;
    }
    
    /**
     * Obtiene la razón social.
     * 
     * @return razón social completa
     */
    public String getRazonSocial() {
        return razonSocial;
    }
    
    /**
     * Establece la razón social.
     * 
     * @param razonSocial razón social de la empresa
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = (razonSocial == null || razonSocial.trim().isEmpty()) ? 
                          this.nombre : razonSocial.trim();
    }
    
    /**
     * Obtiene el representante legal.
     * 
     * @return nombre del representante legal
     */
    public String getRepresentanteLegal() {
        return representanteLegal;
    }
    
    /**
     * Establece el representante legal.
     * 
     * @param representanteLegal nombre del representante legal
     */
    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = (representanteLegal == null) ? "" : representanteLegal.trim();
    }
    
    /**
     * Obtiene el teléfono secundario.
     * 
     * @return teléfono secundario
     */
    public String getTelefonoSecundario() {
        return telefonoSecundario;
    }
    
    /**
     * Establece el teléfono secundario.
     * 
     * @param telefonoSecundario teléfono secundario
     * @throws ValidationException si el teléfono es inválido
     */
    public void setTelefonoSecundario(String telefonoSecundario) throws ValidationException {
        if (telefonoSecundario == null || telefonoSecundario.trim().isEmpty()) {
            this.telefonoSecundario = "";
            return;
        }
        
        String telefonoLimpio = telefonoSecundario.trim();
        if (!PHONE_PATTERN.matcher(telefonoLimpio).matches()) {
            throw ValidationException.formatoInvalido("teléfono secundario", telefonoLimpio, 
                "Solo números, espacios, guiones y signo +");
        }
        
        this.telefonoSecundario = telefonoLimpio;
    }
    
    /**
     * Obtiene la ciudad.
     * 
     * @return ciudad donde opera la empresa
     */
    public String getCiudad() {
        return ciudad;
    }
    
    /**
     * Establece la ciudad.
     * 
     * @param ciudad ciudad donde opera
     */
    public void setCiudad(String ciudad) {
        this.ciudad = (ciudad == null) ? "" : ciudad.trim();
    }
    
    /**
     * Obtiene el departamento.
     * 
     * @return departamento donde opera
     */
    public String getDepartamento() {
        return departamento;
    }
    
    /**
     * Establece el departamento.
     * 
     * @param departamento departamento donde opera
     */
    public void setDepartamento(String departamento) {
        this.departamento = (departamento == null) ? "" : departamento.trim();
    }
    
    /**
     * Obtiene el país.
     * 
     * @return país donde opera
     */
    public String getPais() {
        return pais;
    }
    
    /**
     * Establece el país.
     * 
     * @param pais país donde opera
     */
    public void setPais(String pais) {
        this.pais = (pais == null || pais.trim().isEmpty()) ? "Colombia" : pais.trim();
    }
    
    /**
     * Obtiene el código postal.
     * 
     * @return código postal
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }
    
    /**
     * Establece el código postal.
     * 
     * @param codigoPostal código postal
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = (codigoPostal == null) ? "" : codigoPostal.trim();
    }
    
    /**
     * Obtiene la descripción.
     * 
     * @return descripción de la empresa
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Establece la descripción.
     * 
     * @param descripcion descripción de la empresa
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = (descripcion == null) ? "" : descripcion.trim();
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Obtiene la dirección completa incluyendo ciudad, departamento y país.
     * 
     * @return dirección completa formateada
     */
    public String getDireccionCompleta() {
        StringBuilder direccionCompleta = new StringBuilder(direccion);
        
        if (!ciudad.isEmpty()) {
            direccionCompleta.append(", ").append(ciudad);
        }
        
        if (!departamento.isEmpty()) {
            direccionCompleta.append(", ").append(departamento);
        }
        
        if (!pais.isEmpty()) {
            direccionCompleta.append(", ").append(pais);
        }
        
        if (!codigoPostal.isEmpty()) {
            direccionCompleta.append(" ").append(codigoPostal);
        }
        
        return direccionCompleta.toString();
    }
    
    /**
     * Calcula los años de operación de la empresa.
     * 
     * @return años desde la fundación
     */
    public int calcularAnosOperacion() {
        return LocalDate.now().getYear() - fechaFundacion.getYear();
    }
    
    /**
     * Verifica si la empresa tiene información completa.
     * 
     * @return true si tiene todos los datos básicos
     */
    public boolean tieneInformacionCompleta() {
        return !nombre.isEmpty() && 
               !nit.isEmpty() && 
               !direccion.isEmpty() && 
               !telefono.isEmpty() && 
               !email.isEmpty() &&
               fechaFundacion != null;
    }
    
    /**
     * Genera la firma corporativa para emails.
     * 
     * @return firma corporativa formateada
     */
    public String generarFirmaCorporativa() {
        StringBuilder firma = new StringBuilder();
        firma.append("--\n");
        firma.append(nombre).append("\n");
        
        if (!representanteLegal.isEmpty()) {
            firma.append(representanteLegal).append("\n");
        }
        
        firma.append("📍 ").append(getDireccionCompleta()).append("\n");
        firma.append("📞 ").append(telefono);
        
        if (!telefonoSecundario.isEmpty()) {
            firma.append(" | ").append(telefonoSecundario);
        }
        
        firma.append("\n📧 ").append(email);
        
        if (!sitioWeb.isEmpty()) {
            firma.append("\n🌐 ").append(sitioWeb);
        }
        
        return firma.toString();
    }
    
    /**
     * Valida todos los datos de la empresa.
     * 
     * @return true si todos los datos son válidos
     */
    public boolean validarDatos() {
        try {
            return !nombre.isEmpty() &&
                   !nit.isEmpty() &&
                   !direccion.isEmpty() &&
                   PHONE_PATTERN.matcher(telefono).matches() &&
                   EMAIL_PATTERN.matcher(email).matches() &&
                   fechaFundacion != null &&
                   fechaFundacion.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== MÉTODOS DE PRESENTACIÓN ====================
    
    /**
     * Genera información resumida de la empresa.
     * 
     * @return información resumida
     */
    public String getInformacionResumida() {
        return String.format("%s - NIT: %s - Tel: %s", nombre, nit, telefono);
    }
    
    /**
     * Genera información completa de la empresa.
     * 
     * @return información detallada formateada
     */
    public String mostrarInformacion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INFORMACIÓN DE LA EMPRESA ===\n");
        info.append("Nombre comercial: ").append(nombre).append("\n");
        info.append("Razón social: ").append(razonSocial).append("\n");
        info.append("NIT: ").append(nit).append("\n");
        
        if (!representanteLegal.isEmpty()) {
            info.append("Representante legal: ").append(representanteLegal).append("\n");
        }
        
        info.append("\n--- INFORMACIÓN DE CONTACTO ---\n");
        info.append("Dirección: ").append(getDireccionCompleta()).append("\n");
        info.append("Teléfono principal: ").append(telefono).append("\n");
        
        if (!telefonoSecundario.isEmpty()) {
            info.append("Teléfono secundario: ").append(telefonoSecundario).append("\n");
        }
        
        info.append("Email: ").append(email).append("\n");
        
        if (!sitioWeb.isEmpty()) {
            info.append("Sitio web: ").append(sitioWeb).append("\n");
        }
        
        info.append("\n--- INFORMACIÓN CORPORATIVA ---\n");
        info.append("Fecha de fundación: ").append(fechaFundacion.format(formatter)).append("\n");
        info.append("Años de operación: ").append(calcularAnosOperacion()).append("\n");
        
        if (!descripcion.isEmpty()) {
            info.append("Descripción: ").append(descripcion).append("\n");
        }
        
        if (!logo.isEmpty()) {
            info.append("Logo: ").append(logo).append("\n");
        }
        
        info.append("\nEstado de información: ");
        info.append(tieneInformacionCompleta() ? "✅ Completa" : "⚠️ Incompleta");
        
        return info.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Compara dos empresas por su NIT.
     * 
     * @param obj objeto a comparar
     * @return true si tienen el mismo NIT
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Empresa empresa = (Empresa) obj;
        return Objects.equals(nit, empresa.nit);
    }
    
    /**
     * Genera código hash basado en el NIT.
     * 
     * @return código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(nit);
    }
    
    /**
     * Representación en cadena de la empresa.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Empresa{nombre='%s', nit='%s', telefono='%s', email='%s'}", 
                           nombre, nit, telefono, email);
    }
}