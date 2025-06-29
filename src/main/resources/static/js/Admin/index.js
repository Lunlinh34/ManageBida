document.addEventListener('DOMContentLoaded', function () {
    initializeStoreFilters();

    const filterBadges = document.querySelectorAll('.filter-badge');
    const tableItems = document.querySelectorAll('.table-item');
    const emptyStatePoor = document.getElementById('emptyStatePoor');
    const emptyStateCarom = document.getElementById('emptyStateCarom');
    const resetButtons = document.querySelectorAll('.reset-filters');
    const backToTopBtn = document.querySelector('.back-to-top');

    let filters = {
        store: 'all',
        type: 'all',
        status: 'all',
        vip: 'all'
    };

    filterBadges.forEach(badge => {
        badge.addEventListener('click', function () {
            const filterGroup = this.closest('div');
            filterGroup.querySelectorAll('.filter-badge').forEach(b => {
                b.classList.remove('active');
            });
            this.classList.add('active');

            if (this.dataset.store) filters.store = this.dataset.store;
            if (this.dataset.type) filters.type = this.dataset.type;
            if (this.dataset.status) filters.status = this.dataset.status;
            if (this.dataset.vip) filters.vip = this.dataset.vip;

            applyFilters();
        });
    });

    resetButtons.forEach(button => {
        button.addEventListener('click', function () {
            filters = {
                store: 'all',
                type: 'all',
                status: 'all',
                vip: 'all'
            };

            filterBadges.forEach(badge => {
                const parent = badge.parentElement;
                const dataAttr = Array.from(parent.classList)[0].split('-')[0];
                
                if (badge.dataset[dataAttr] === 'all') {
                    badge.classList.add('active');
                } else {
                    badge.classList.remove('active');
                }
            });

            applyFilters();
        });
    });

    function initializeStoreFilters() {
        const storeFiltersContainer = document.getElementById('storeFilters');
        const stores = new Set();
        const storeNames = {};
        
        document.querySelectorAll('.table-item').forEach(item => {
            const storeId = item.dataset.store;
            if (storeId) {
                stores.add(storeId);
                const storeName = item.querySelector('.store-name').textContent;
                storeNames[storeId] = storeName;
            }
        });

        stores.forEach(storeId => {
            const badge = document.createElement('span');
            badge.className = 'filter-badge';
            badge.dataset.store = storeId;
            badge.textContent = storeNames[storeId] || `Cửa hàng ${storeId}`;
            storeFiltersContainer.appendChild(badge);
            

            badge.addEventListener('click', function() {
                storeFiltersContainer.querySelectorAll('.filter-badge').forEach(b => {
                    b.classList.remove('active');
                });
                this.classList.add('active');
                
                filters.store = this.dataset.store;
                applyFilters();
            });
        });
    }

    function applyFilters() {
        let poorVisible = 0;
        let caromVisible = 0;

        tableItems.forEach(item => {
            let isVisible = true;

            if (filters.store !== 'all' && item.dataset.store !== filters.store) {
                isVisible = false;
            }
            if (filters.type !== 'all' && item.dataset.type !== filters.type) {
                isVisible = false;
            }
            if (filters.status !== 'all' && item.dataset.status !== filters.status) {
                isVisible = false;
            }
            if (filters.vip !== 'all' && item.dataset.vip !== filters.vip) {
                isVisible = false;
            }

            if (isVisible) {
                item.classList.remove('animate__fadeOut');
                item.classList.add('animate__fadeIn');
                setTimeout(() => {
                    item.style.display = 'block';
                }, 300);

                if (item.dataset.type === 'poor') {
                    poorVisible++;
                } else if (item.dataset.type === 'carom') {
                    caromVisible++;
                }
            } else {
                item.classList.remove('animate__fadeIn');
                item.classList.add('animate__fadeOut');
                setTimeout(() => {
                    item.style.display = 'none';
                }, 300);
            }
        });

        emptyStatePoor.style.display = poorVisible === 0 ? 'block' : 'none';
        emptyStateCarom.style.display = caromVisible === 0 ? 'block' : 'none';
    }
});