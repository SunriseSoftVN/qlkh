/**
 * Deploys different versions of the applet depending on Java version.
 * Useful for removing warning dialogs for Java 6.  This function is optional
 * however, if used, should replace the <applet> method.  Needed to address
 * MANIFEST.MF TrustedLibrary=true discrepency between JRE6 and JRE7.
 */
function deployQZ() {
    var attributes = {id: "qz", code: 'qz.PrintApplet.class',
        archive: 'qz-print.jar', width: 1, height: 1};
    var parameters = {jnlp_href: 'qz-print_jnlp.jnlp',
        cache_option: 'plugin', disable_logging: 'false',
        initial_focus: 'false'};
    if (deployJava.versionCheck("1.7+") == true) {
    }
    else if (deployJava.versionCheck("1.6+") == true) {
        attributes['archive'] = 'jre6/qz-print.jar';
        parameters['jnlp_href'] = 'jre6/qz-print_jnlp.jnlp';
    }
    deployJava.runApplet(attributes, parameters, '1.5');
}

/**
 * Automatically gets called when applet has loaded.
 */
function qzReady() {
    // Setup our global qz object
    window["qz"] = document.getElementById('qz');
    if (qz) {
        useDefaultPrinter();
    }
}

/**
 * Returns whether or not the applet is not ready to print.
 * Displays an alert if not ready.
 */
function notReady() {
    // If applet is not loaded, display an error.
    if (!qz) {
        alert('Error:\n\n\tApplet is not loaded!');
        return true;
    }
    // If a printer hasn't been selected, display a message.
    else if (!qz.getPrinter()) {
        alert('Please select a printer first by using the "Detect Printer" button.');
        return true;
    }
    return false;
}

/***************************************************************************
 * Prototype function for listing all printers attached to the system
 * Usage:
 *    qz.findPrinter('\\{dummy_text\\}');
 *    window['qzDoneFinding'] = function() { alert(qz.getPrinters()); };
 ***************************************************************************/
function findPrinters() {
    if (qz) {
        // Searches for a locally installed printer with a bogus name
        qz.findPrinter('\\{bogus_printer\\}');
    } else {
        // If applet is not loaded, display an error.
        return alert('Error:\n\n\tApplet is not loaded!');
    }

    // Automatically gets called when "qz.findPrinter()" is finished.
    window['qzDoneFinding'] = function () {
        // Get the CSV listing of attached printers
        var printers = qz.getPrinters().split(',');
        for (i in printers) {
            alert(printers[i] ? printers[i] : 'Unknown');
        }

        // Remove reference to this function
        window['qzDoneFinding'] = null;
    };
}

/***************************************************************************
 * Prototype function for finding the "default printer" on the system
 * Usage:
 *    qz.findPrinter();
 *    window['qzDoneFinding'] = function() { alert(qz.getPrinter()); };
 ***************************************************************************/
function useDefaultPrinter() {
    if (qz) {
        // Searches for default printer
        qz.findPrinter();
    }

    // Automatically gets called when "qz.findPrinter()" is finished.
    window['qzDoneFinding'] = function () {
        // Alert the printer name to user
        var printer = qz.getPrinter();
        alert(printer !== null ? 'Default printer found: "' + printer :
            'Default printer ' + 'not found');

        // Remove reference to this function
        window['qzDoneFinding'] = null;
    };
}