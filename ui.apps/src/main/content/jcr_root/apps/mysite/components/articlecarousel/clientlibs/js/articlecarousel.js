(function (document) {
    "use strict";

    /**
     * The main initialization function
     */
    function initCarousel() {
        const tracks = document.querySelectorAll('.article-carousel-track');
        if (!tracks.length) {
            return;
        }

        tracks.forEach(track => initializeTrack(track));
    }

    function initializeTrack(track) {
        // Prevent double-initialization for the same element
        if (track.dataset.initialized === "true") {
            return;
        }

        const rootPath = track.getAttribute('data-path');
        if (!rootPath || rootPath.trim() === "") {
            track.innerHTML = "<p style='padding:20px; color:#666;'>Please configure the Article Root Path in the component dialog.</p>";
            return;
        }

        const servletUrl = `/bin/fetchSitePages?path=${encodeURIComponent(rootPath)}&_=${new Date().getTime()}`;

        fetch(servletUrl)
            .then(response => {
                if (!response.ok) throw new Error("Servlet response was not ok");
                return response.json();
            })
            .then(data => {
                if (data.status === "success" && Array.isArray(data.pages) && data.pages.length > 0) {
                    renderArticles(data.pages, track);
                    track.dataset.initialized = "true";
                } else {
                    track.innerHTML = "<p style='padding:20px;'>No pages found at the selected path.</p>";
                }
            })
            .catch(err => {
                console.error("Error loading articles:", err);
                track.innerHTML = "<p style='padding:20px; color:red;'>Error connecting to article service.</p>";
            });
    }

    /**
     * Renders the JSON data into HTML cards
     */
    function renderArticles(pages, container) {
        const html = pages.map(page => `
            <div class="carousel-card" style="flex: 0 0 300px; margin-right: 20px;">
                <div class="card-content" style="border: 1px solid #ddd; padding: 15px; border-radius: 8px;">
                    <h4 style="margin-top:0;">${page.title}</h4>
                    <p style="font-size: 0.85em; color: #777;">Path: ${page.path}</p>
                    <a href="${page.path}.html" class="read-more-btn">Read Article</a>
                </div>
            </div>
        `).join('');

        container.innerHTML = html;
        container.style.display = "flex";
        container.style.overflowX = "auto";

        setupSliderButtons(container);
    }

    /**
     * Adds event listeners to Prev/Next buttons if they exist
     */
    function setupSliderButtons(container) {
        const wrapper = container.closest('.article-carousel-wrapper');
        if (!wrapper) return;

        const nextBtn = wrapper.querySelector('.next-btn');
        const prevBtn = wrapper.querySelector('.prev-btn');

        if (nextBtn) {
            nextBtn.addEventListener('click', () => {
                container.scrollBy({ left: 320, behavior: 'smooth' });
            });
        }
        if (prevBtn) {
            prevBtn.addEventListener('click', () => {
                container.scrollBy({ left: -320, behavior: 'smooth' });
            });
        }
    }

    // --- EXECUTION LOGIC ---
    if (document.readyState === "complete" || document.readyState === "interactive") {
        initCarousel();
    } else {
        document.addEventListener("DOMContentLoaded", initCarousel);
    }

    if (window.Granite && window.Granite.author) {
        document.addEventListener("cq-content-loaded", initCarousel);
    }

})(document);