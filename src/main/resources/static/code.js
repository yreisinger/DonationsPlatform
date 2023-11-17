window.onload = () => {
    getAllAds()
}

function getAllAds()    {
    fetch("/ad")
        .then(response => {
            if(response.status !== 200) {
                throw new Error(response.statusText)
            }

            return response.json()
        })
        .then(data => {
            let table = document.getElementById("adsTable");

            table.innerHTML = `<tr><th>ID</th><th>Description</th><th>Picture</th><th>Wallet</th></tr>`

            data.forEach(ad => {
                table.innerHTML += `<tr><td>${ad.adId}</td><td>${ad.description}</td><td>${ad.picture}</td><td>${ad.wallet}</td></tr>`
            })
        })
        .catch(err => {
            console.log(err)
        })
}