function loadLinkButton() {
	$(".link-button,.nav-button").click(function() {
		if ($(this).data("href") !== "/logout") {
			$("main").empty().load($(this).data("href"));
		} else {
			window.location.href = "/logout";
		}
	});
};

function loadCategories() {
	$.ajax({
		url: 'data/categories',
		method: 'GET',
		success: function(data) {
			$("#category-selector").empty();
			$("#category-selector").append(`
							<option value="" selected disabled>Select a category</option>
					`);
			data.forEach(function(category) {
				$("#category-selector").append(`
								<option value="${category.categoryName}">${category.categoryName}</option>
						`);
			});
			$("#category-selector").append(`
							<option disabled></option>
					`);
			$("#category-selector").append(`
							<option value="Add Category" style="cursor:pointer">Quick add new Category</option>
					`);
		},
		error: function(xhr, status, error) {
			console.error("Error:", error);
			$("#category-selector").append(`
							<option>Fail to load category</option>
					`);
			$("#category-selector").prop('disabled', true);
		}
	});
};

function setupCategoryChangeHandler() {
	$("#category-selector").on("change", function() {
		if ($(this).val() === "Add Category") {
			$("#modal-container").empty().load("/admin/add-category.html");
		}
	});
};

window.loadLinkButton = loadLinkButton;
window.loadCategories = loadCategories;
window.setupCategoryChangeHandler = setupCategoryChangeHandler;
