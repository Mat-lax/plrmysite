(function ($, window, document) {
    "use strict";

    var registry = $(window).adaptTo("foundation-registry");

    // IMAGE VALIDATOR
    registry.register("foundation.validation.validator", {
        selector: "[name='./imagePath']", // Targeting the name directly is more reliable
        validate: function(el) {
            var $el = $(el);
            var path = $el.val();
            
            // Only validate if this field is currently VISIBLE (since you use show/hide)
            if ($el.closest('.media-target-container').hasClass('hide') || !path) {
                return;
            }

            console.log("Validating Image Path: " + path);

            var isImage = /\.(jpg|jpeg|png|gif|svg|webp)$/i.test(path);
            if (!isImage) {
                return "Invalid Format: Please select an Image file (jpg, png, etc).";
            }
        }
    });

    // VIDEO VALIDATOR
    registry.register("foundation.validation.validator", {
        selector: "[name='./videoPath']", // Targeting the name directly
        validate: function(el) {
            var $el = $(el);
            var path = $el.val();

            if ($el.closest('.media-target-container').hasClass('hide') || !path) {
                return;
            }

            console.log("Validating Video Path: " + path);

            var isVideo = /\.(mp4|mov|wmv|avi)$/i.test(path);
            if (!isVideo) {
                return "Invalid Format: Please select a Video file (mp4, mov, etc).";
            }
        }
    });

})(Granite.$, window, document);