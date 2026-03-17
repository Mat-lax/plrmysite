(function ($, window, document) {
    "use strict";

    var registry = $(window).adaptTo("foundation-registry");

    // Register a validator named "hero.mediapath.warning"
    registry.register("foundation.validation.validator", {
        selector: "[data-validation='hero.mediapath.warning']",
        validate: function (el) {
            var field = $(el);
            var $dialog = field.closest("coral-dialog-content");
            
            // 1. Get current dropdown value
            var mediaType = $dialog.find("[name='./mediaType']").val();
            var value = field.val();

            // 2. Logic: If "Image" is selected but field is empty
            if (field.attr("name") === "./imagePath" && mediaType === "image" && !value) {
                // Returning a string creates a RED error/warning in AEM
                return "Warning: You have selected 'Image' but the path is empty. Please verify.";
            }

            // 3. Logic: If "Video" is selected but field is empty
            if (field.attr("name") === "./videoPath" && mediaType === "video" && !value) {
                return "Warning: You have selected 'Video' but no video path is provided.";
            }
        }
    });
}(jQuery, window, document));