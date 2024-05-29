document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('div[id^="orderModal"]').forEach(function(modal) {
        const lapPrice = modal.dataset.lapPrice;

        modal.addEventListener('shown.bs.modal', function() {
            const nLapsInput = modal.querySelector('input[name="nLaps"]');
            const priceInput = modal.querySelector('#orderPrice' + modal.id.replace('orderModal', ''));

            nLapsInput.addEventListener('input', function() {
                const nLaps = parseInt(nLapsInput.value, 10);
                let newPrice;
                if (isNaN(nLaps)) {
                    newPrice = 0;
                } else {
                    newPrice = lapPrice * nLaps;
                }
                priceInput.value = newPrice;
            });
        });
    });
});