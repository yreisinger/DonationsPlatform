window.onload = () => {
    getAllAds()
}

function getAllAds()    {
    fetch(`/ad`)
        .then(response => {
            if(response.status !== 200) {
                throw new Error(response.statusText)
            }

            return response.json()
        })
        .then(data => {
            let table = document.getElementById("adsTable");

            table.innerHTML = `<tr><th>Description</th><th>Picture</th><th>Details</th></tr>`

            data.forEach(ad => {
                table.innerHTML += `<tr><td>${ad.description}</td><td>${ad.picture}</td><td><button onclick="getAdDetails(${ad.adId})">Details</button></td></tr>`
            })
        })
        .catch(err => {
            console.log(err)
        })
}

function getAdDetails(id) {
    fetch(`/ad/${id}`)
        .then(response => {
            if(response.status !== 200) {
                throw new Error(response.statusText)
            }

            return response.json()
        })
        .then(data => {
            let div = document.getElementById("adDetails")

            div.innerHTML = `<p>ID: ${data.adId} | Wallet: ${data.wallet}</p>`
        })
        .catch(err => {
            console.log(err)
        })
}