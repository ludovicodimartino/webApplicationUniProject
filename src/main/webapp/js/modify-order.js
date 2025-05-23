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

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('[id^="orderDate"]').forEach(function(orderDate) {
        const orderId = orderDate.id.replace('orderDate', '');

        orderDate.addEventListener("input", function() {
            const selectedDate = new Date(orderDate.value);
            const currentDate = new Date();
            currentDate.setHours(0, 0, 0, 0);
            const timeDifference = selectedDate - currentDate;
            const dayDifference = timeDifference / (1000 * 60 * 60 * 24);

            if (dayDifference < 2) {
                orderDate.classList.add("is-invalid");
            } else {
                orderDate.classList.remove("is-invalid");
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll('[id^="nLaps"]').forEach(function(orderNLaps) {
        const orderId = orderNLaps.id.replace('nLaps', '');

        orderNLaps.addEventListener("input", function() {
            const nLaps = parseInt(orderNLaps.value);

            if (nLaps <= 0 || isNaN(nLaps)) {
                orderNLaps.classList.add("is-invalid");
            } else {
                orderNLaps.classList.remove("is-invalid");
            }
        });
    });
});
