/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
// app.js - Funcionalidades JavaScript

$(document).ready(function() {
    
    // Inicializar tooltips
    $('[data-bs-toggle="tooltip"]').tooltip();
    
    // Inicializar popovers
    $('[data-bs-toggle="popover"]').popover();
    
    // Confirmación para acciones peligrosas
    $('.btn-delete, .btn-danger').on('click', function(e) {
        if (!confirm('¿Estás seguro de realizar esta acción?')) {
            e.preventDefault();
            return false;
        }
    });
    
    // Auto-dismiss alerts después de 5 segundos
    setTimeout(function() {
        $('.alert').alert('close');
    }, 5000);
    
    // Validación de formularios personalizada
    $('form').on('submit', function() {
        const form = $(this);
        if (form[0].checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.addClass('was-validated');
    });
    
    // Toggle password visibility
    $('.toggle-password').on('click', function() {
        const input = $(this).parent().find('input');
        const icon = $(this).find('i');
        
        if (input.attr('type') === 'password') {
            input.attr('type', 'text');
            icon.removeClass('bi-eye').addClass('bi-eye-slash');
        } else {
            input.attr('type', 'password');
            icon.removeClass('bi-eye-slash').addClass('bi-eye');
        }
    });
    
    // Función para mostrar loading en botones
    $('.btn-loading').on('click', function() {
        const btn = $(this);
        btn.prop('disabled', true);
        btn.html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Procesando...');
    });
    
    // Funcionalidad para búsqueda en tiempo real
    $('#searchInput').on('keyup', function() {
        const value = $(this).val().toLowerCase();
        $('table tbody tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
    
    // Actualizar contador de caracteres en textareas
    $('textarea[maxlength]').on('input', function() {
        const maxLength = $(this).attr('maxlength');
        const currentLength = $(this).val().length;
        const counter = $(this).siblings('.char-counter');
        
        if (counter.length === 0) {
            $(this).after('<small class="text-muted char-counter float-end">' + currentLength + '/' + maxLength + '</small>');
        } else {
            counter.text(currentLength + '/' + maxLength);
            
            if (currentLength >= maxLength * 0.9) {
                counter.addClass('text-warning');
            } else {
                counter.removeClass('text-warning');
            }
        }
    }).trigger('input');
    
    // Función para copiar al portapapeles
    $('.copy-to-clipboard').on('click', function() {
        const text = $(this).data('text');
        navigator.clipboard.writeText(text).then(function() {
            alert('Copiado al portapapeles');
        }, function(err) {
            console.error('Error al copiar: ', err);
        });
    });
    
    // Función para exportar tabla a CSV
    $('.export-csv').on('click', function() {
        const table = $(this).data('table');
        const rows = $(table + ' tbody tr');
        let csv = [];
        
        // Encabezados
        const headers = [];
        $(table + ' thead th').each(function() {
            headers.push($(this).text().trim());
        });
        csv.push(headers.join(','));
        
        // Datos
        rows.each(function() {
            const row = [];
            $(this).find('td').each(function() {
                row.push($(this).text().trim().replace(/,/g, ''));
            });
            csv.push(row.join(','));
        });
        
        // Descargar
        const csvContent = csv.join('\n');
        const blob = new Blob([csvContent], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'export.csv';
        a.click();
        window.URL.revokeObjectURL(url);
    });
});

// Función para formatear fechas
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Función para validar email
function isValidEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Función para mostrar notificación
function showNotification(message, type = 'info') {
    const alert = $('<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                   '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>' +
                   message + '</div>');
    
    $('.alerts-container').append(alert);
    
    setTimeout(function() {
        alert.alert('close');
    }, 3000);
}