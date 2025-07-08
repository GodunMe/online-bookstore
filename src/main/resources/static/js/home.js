function implAddToCartButton() {
	$(".add-to-cart-btn").click(function(e) {
		e.preventDefault();
		const bookId = $(this).data("book-id");
		
		const formData = new FormData();
		formData.append('bookId', bookId);
		formData.append('quantity', 1);
		$.ajax({
			url: '/session/cart/add',
			method: 'POST',
			data: formData,
			processData: false,
			contentType: false,
			success: function(message) {
				$("#notification").text(message).addClass("text-green-600").fadeIn().delay(2000).fadeOut();
			},
			error: function(xhr, status, error) {
				alert(xhr.responseText);
				console.error("Error:", error);
			}
		});
	});
}

window.implAddToCartButton = implAddToCartButton;

function initLayoutBehavior() {
    const $buttons = $(".sidebar-btn");
    const $categoriesBtn = $("#categories-btn");
    const $categoriesMenu = $("#categories-menu");
    const $footer = $("#footer");
    const $mainContent = $("#main-content");
    const $sidebar = $("#sidebar");
    const $userMenuBtn = $("#userMenuButton");
    const $userDropdown = $("#userDropdown");

    let $currentActiveBtn = null;

    // Highlight current page
	const currentPage = window.location.pathname.split('/').pop();
	let currentActiveBtn = null;

	$('a').each(function () {
	    const href = $(this).attr('href');
	    if (href) {
	        const page = href.split('/').pop();

	        if (
	            page === currentPage ||
	            (page === 'help' && ['order-guide', 'shipping-methods', 'payment-guide'].includes(currentPage))
	        ) {
	            $(this).addClass('bg-gray-200');
	            currentActiveBtn = $(this);
	        }
	    }
	});


    // Handle nav button clicks
    $buttons.each(function () {
        const $btn = $(this);
        const href = $btn.attr("href");
        if (href) {
            $btn.on("click", function () {
                $buttons.removeClass("bg-gray-200");
                $btn.addClass("bg-gray-200");
                $currentActiveBtn = $btn;
            });
        }
    });

    // Toggle Categories Menu
    $categoriesBtn.on("click", function (e) {
        e.stopPropagation();
        $categoriesMenu.toggleClass("hidden");

        $buttons.removeClass("bg-gray-200");
        $categoriesBtn.addClass("bg-gray-200");
    });

    // Close dropdowns on click outside
    $(window).on("click", function (e) {
        if (!$(e.target).closest("#categories-btn, #categories-menu").length) {
            $categoriesMenu.addClass("hidden");
            $buttons.removeClass("bg-gray-200");
            if ($currentActiveBtn) {
                $currentActiveBtn.addClass("bg-gray-200");
            }
        }

        if (!$(e.target).closest("#userMenuButton, #userDropdown").length) {
            $userDropdown.addClass("hidden");
        }
    });

    // Footer show/hide
    function toggleFooter(visible) {
        $footer.css({
            opacity: visible ? "1" : "0",
            pointerEvents: visible ? "auto" : "none"
        });
    }

    // Show footer when scrolling to bottom of main content
    $mainContent.on("scroll", function () {
        const scrollTop = $(this).scrollTop();
        const scrollHeight = this.scrollHeight;
        const clientHeight = this.clientHeight;

        toggleFooter(scrollTop + clientHeight >= scrollHeight - 10);
    });

    // Show/hide footer based on scroll in sidebar
    let lastSidebarScrollTop = 0;
    $sidebar.on("scroll", function () {
        const scrollTop = $(this).scrollTop();
        const scrollDown = scrollTop > lastSidebarScrollTop;
        toggleFooter(scrollDown);
        lastSidebarScrollTop = scrollTop;
    });

    // Show footer on short page
    $(window).on("load", function () {
        const mainHeight = $mainContent[0].scrollHeight;
        const windowHeight = $(window).height();
        if (mainHeight < windowHeight) {
            toggleFooter(true);
        }
    });

    // Toggle user dropdown
    $userMenuBtn.on("click", function () {
        $userDropdown.toggleClass("hidden");
    });

    // Clear search
    window.clearSearch = function () {
        const $input = $("#searchInput");
        $input.val("");
        toggleClearButton();
        $input.focus();
    };

    window.toggleClearButton = function () {
        const $input = $("#searchInput");
        const $clearBtn = $("#clearButton");
        if ($input.val().length > 0) {
            $clearBtn.removeClass("hidden");
        } else {
            $clearBtn.addClass("hidden");
        }
    };
}

// Export globally
window.initLayoutBehavior = initLayoutBehavior;
