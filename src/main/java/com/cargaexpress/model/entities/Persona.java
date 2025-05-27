package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase abstracta que representa una persona en el sistema.
 * Contiene los atributos y métodos comunes para Cliente y Conductor.
 * Implementa validaciones básicas y funcionalidades compartidas.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public abstract class Persona {
    
    // ==================== ATRIBUTOS PROTEGIDOS ====================
    
    /**
     * Número de cédula de la persona (único identificador)
     */
    protected String cedula;
    
    /**
     * Nombre de la persona
     */
    protected String nombre;
    
    /**
     * Apellido de la persona
     */
    protected String apellido;
    
    /**
     * Número de teléfono de contacto
     */
    protected String telefono;
    
    /**
     * Dirección de correo electrónico
     */
    protected String email;
    
    /**
     * Fecha de nacimiento
     */
    protected LocalDate fechaNacimiento;
    
    // ==================== PATRONES DE VALIDACIÓN ====================
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(Constants.EMAIL_PATTERN);
    private static final Pattern PHONE_PATTERN = Pattern.compile(Constants.PHONE_PATTERN);
    private static final Pattern CEDULA_PATTERN = Pattern.compile(Constants.CEDULA_PATTERN);
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa una persona con valores por defecto.
     */
    public Persona() {
        this.cedula = "";
        this.nombre = "";
        this.apellido = "";
        this.telefono = "";
        this.email = "";
        this.fechaNacimiento = LocalDate.now().minusYears(18); // Edad mínima 18 años
    }
    
    /**
     * Constructor completo con validaciones.
     * Crea una persona con todos los datos básicos.
     * 
     * @param cedula número de cédula
     * @param nombre nombre de la persona
     * @param apellido apellido de la persona
     * @param telefono número de teléfono
     * @param email dirección de correo electrónico
     * @param fechaNacimiento fecha de nacimiento
     * @throws ValidationException si algún dato es inválido
     */
    public Persona(String cedula, String nombre, String apellido, String telefono, 
                   String email, LocalDate fechaNacimiento) throws ValidationException {
        setCedula(cedula);
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
        setEmail(email);
        setFechaNacimiento(fechaNacimiento);
    }
    
    // ==================== GETTERS Y SETTERS CON VALIDACIÓN ====================
    
    /**
     * Obtiene el número de cédula.
     * 
     * @return número de cédula
     */
    public String getCedula() {
        return cedula;
    }
    
    /**
     * Establece el número de cédula con validación.
     * 
     * @param cedula número de cédula a establecer
     * @throws ValidationException si la cédula es inválida
     */
    public void setCedula(String cedula) throws ValidationException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw ValidationException.campoRequerido("cédula");
        }
        
        String cedulaLimpia = cedula.trim();
        if (!CEDULA_PATTERN.matcher(cedulaLimpia).matches()) {
            throw ValidationException.formatoInvalido("cédula", cedulaLimpia, "8-10 dígitos numéricos");
        }
        
        this.cedula = cedulaLimpia;
    }
    
    /**
     * Obtiene el nombre.
     * 
     * @return nombre de la persona
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre con validación y formato.
     * 
     * @param nombre nombre a establecer
     * @throws ValidationException si el nombre es inválido
     */
    public void setNombre(String nombre) throws ValidationException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw ValidationException.campoRequerido("nombre");
        }
        
        String nombreLimpio = nombre.trim();
        if (nombreLimpio.length() < Constants.MIN_NAME_LENGTH || 
            nombreLimpio.length() > Constants.MAX_NAME_LENGTH) {
            throw ValidationException.longitudInvalida("nombre", nombreLimpio, 
                Constants.MIN_NAME_LENGTH, Constants.MAX_NAME_LENGTH);
        }
        
        // Formatear nombre: Primera letra mayúscula, resto minúscula
        this.nombre = formatearNombre(nombreLimpio);
    }
    
    /**
     * Obtiene el apellido.
     * 
     * @return apellido de la persona
     */
    public String getApellido() {
        return apellido;
    }
    
    /**
     * Establece el apellido con validación y formato.
     * 
     * @param apellido apellido a establecer
     * @throws ValidationException si el apellido es inválido
     */
    public void setApellido(String apellido) throws ValidationException {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw ValidationException.campoRequerido("apellido");
        }
        
        String apellidoLimpio = apellido.trim();
        if (apellidoLimpio.length() < Constants.MIN_NAME_LENGTH || 
            apellidoLimpio.length() > Constants.MAX_NAME_LENGTH) {
            throw ValidationException.longitudInvalida("apellido", apellidoLimpio, 
                Constants.MIN_NAME_LENGTH, Constants.MAX_NAME_LENGTH);
        }
        
        // Formatear apellido: Primera letra mayúscula, resto minúscula
        this.apellido = formatearNombre(apellidoLimpio);
    }
    
    /**
     * Obtiene el número de teléfono.
     * 
     * @return número de teléfono
     */
    public String getTelefono() {
        return telefono;
    }
    
    /**
     * Establece el número de teléfono con validación.
     * 
     * @param telefono número de teléfono a establecer
     * @throws ValidationException si el teléfono es inválido
     */
    public void setTelefono(String telefono) throws ValidationException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw ValidationException.campoRequerido("teléfono");
        }
        
        String telefonoLimpio = telefono.trim();
        if (telefonoLimpio.length() < Constants.MIN_PHONE_LENGTH || 
            telefonoLimpio.length() > Constants.MAX_PHONE_LENGTH) {
            throw ValidationException.longitudInvalida("teléfono", telefonoLimpio, 
                Constants.MIN_PHONE_LENGTH, Constants.MAX_PHONE_LENGTH);
        }
        
        if (!PHONE_PATTERN.matcher(telefonoLimpio).matches()) {
            throw ValidationException.formatoInvalido("teléfono", telefonoLimpio, 
                "Solo números, espacios, guiones y signo +");
        }
        
        this.telefono = telefonoLimpio;
    }
    
    /**
     * Obtiene la dirección de correo electrónico.
     * 
     * @return dirección de email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Establece el email con validación de formato.
     * 
     * @param email dirección de correo electrónico
     * @throws ValidationException si el email es inválido
     */
    public void setEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw ValidationException.campoRequerido("email");
        }
        
        String emailLimpio = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(emailLimpio).matches()) {
            throw ValidationException.formatoInvalido("email", emailLimpio, 
                "formato: usuario@dominio.com");
        }
        
        this.email = emailLimpio;
    }
    
    /**
     * Obtiene la fecha de nacimiento.
     * 
     * @return fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    /**
     * Establece la fecha de nacimiento con validación de edad.
     * 
     * @param fechaNacimiento fecha de nacimiento
     * @throws ValidationException si la fecha es inválida
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) throws ValidationException {
        if (fechaNacimiento == null) {
            throw ValidationException.campoRequerido("fecha de nacimiento");
        }
        
        // Validar que no sea fecha futura
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ValidationException("fecha de nacimiento", fechaNacimiento.toString(), 
                "No puede ser fecha futura");
        }
        
        // Validar edad mínima (18 años)
        int edad = calcularEdad(fechaNacimiento);
        if (edad < 18) {
            throw new ValidationException("fecha de nacimiento", fechaNacimiento.toString(), 
                "Edad mínima requerida: 18 años");
        }
        
        // Validar edad máxima razonable (120 años)
        if (edad > 120) {
            throw new ValidationException("fecha de nacimiento", fechaNacimiento.toString(), 
                "Edad máxima permitida: 120 años");
        }
        
        this.fechaNacimiento = fechaNacimiento;
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Obtiene el nombre completo (nombre + apellido).
     * 
     * @return nombre completo formateado
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    /**
     * Calcula la edad actual basada en la fecha de nacimiento.
     * 
     * @return edad en años
     */
    public int calcularEdad() {
        return calcularEdad(this.fechaNacimiento);
    }
    
    /**
     * Calcula la edad basada en una fecha específica.
     * 
     * @param fechaNacimiento fecha de nacimiento
     * @return edad en años
     */
    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    /**
     * Valida si el email tiene formato correcto.
     * 
     * @return true si el email es válido
     */
    public boolean validarEmail() {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida si el teléfono tiene formato correcto.
     * 
     * @return true si el teléfono es válido
     */
    public boolean validarTelefono() {
        return telefono != null && PHONE_PATTERN.matcher(telefono).matches();
    }
    
    /**
     * Valida si la cédula tiene formato correcto.
     * 
     * @return true si la cédula es válida
     */
    public boolean validarCedula() {
        return cedula != null && CEDULA_PATTERN.matcher(cedula).matches();
    }
    
    /**
     * Valida todos los datos básicos de la persona.
     * 
     * @return true si todos los datos son válidos
     */
    public boolean validarDatos() {
        return validarCedula() && validarEmail() && validarTelefono() &&
               nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               fechaNacimiento != null;
    }
    
    // ==================== MÉTODOS UTILITARIOS ====================
    
    /**
     * Formatea un nombre: Primera letra mayúscula, resto minúscula.
     * 
     * @param nombre nombre a formatear
     * @return nombre formateado
     */
    private String formatearNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "";
        }
        
        String nombreLimpio = nombre.trim().toLowerCase();
        return nombreLimpio.substring(0, 1).toUpperCase() + nombreLimpio.substring(1);
    }
    
    // ==================== MÉTODOS ABSTRACTOS ====================
    
    /**
     * Método abstracto para mostrar información específica de cada tipo de persona.
     * Debe ser implementado por las clases Cliente y Conductor.
     * 
     * @return información formateada de la persona
     */
    public abstract String mostrarInformacion();
    
    // ==================== MÉTODOS SOBRESCRITOS DE OBJECT ====================
    
    /**
     * Compara dos personas por su cédula (identificador único).
     * 
     * @param obj objeto a comparar
     * @return true si tienen la misma cédula
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Persona persona = (Persona) obj;
        return Objects.equals(cedula, persona.cedula);
    }
    
    /**
     * Genera código hash basado en la cédula.
     * 
     * @return código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }
    
    /**
     * Representación en cadena básica de la persona.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Persona{cedula='%s', nombre='%s %s', email='%s'}", 
                           cedula, nombre, apellido, email);
    }
}