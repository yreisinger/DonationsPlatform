function getAllAds(chioce)    {
    fetch(`/ad`)
        .then(response => {
            if(response.status !== 200) {
                throw new Error(response.statusText)
            }

            return response.json()
        })
        .then(data => {
            if(chioce === "latest") {
                fillLatestCarousel(data)
            }else {
                fillCarousel(data)
            }
        })
        .catch(err => {
            console.log(err)
        })
}

function fillCarousel(data) {
    let mainDiv = document.getElementById('carousel')

    for (let i = 0; i < data.length; i += 3) {
        let newCarousel = document.createElement('div')

        newCarousel.classList.add('carousel')

        for (let j = i; j < i + 3 && j < data.length; j++) {
            let adContainer = document.createElement('button')
            let img = document.createElement('img')
            let description = document.createElement('p')

            adContainer.classList.add('carousel-item')


            adContainer.addEventListener('click', async () => {
                function storage() {
                    localStorage.setItem("adId", data[j].adId)
                }

                await storage()

                location.href = "adDetails.html"
            })

            description.innerText = data[j].description.substring(0, 50)
            img.src = data[j].picture

            adContainer.appendChild(img)
            adContainer.appendChild(description)

            newCarousel.appendChild(adContainer)
        }

        mainDiv.insertAdjacentElement('afterend', newCarousel)
    }
}

function fillLatestCarousel(data)   {
    data = data.reverse()

    let carouselItem = document.getElementById('latest_carousel_item')

    for (let i = 0; i < 3; i++)    {
        let adContainer = document.createElement('button')
        let img = document.createElement('img')
        let description = document.createElement('p')

        adContainer.classList.add('carousel-item')

        adContainer.addEventListener('click', async () => {
            function storage() {
                localStorage.setItem("adId", data[i].adId)
            }

            await storage()

            location.href = "adDetails.html"
        })

        description.innerText = data[i].description.substring(0, 50)
        img.src = data[i].picture

        adContainer.appendChild(img)
        adContainer.appendChild(description)

        carouselItem.appendChild(adContainer)
    }
}

function getAdDetails() {
    let id = localStorage.getItem("adId")

    fetch(`/ad/${id}`)
        .then(response => {
            if(response.status !== 200) {
                throw new Error(response.statusText)
            }

            return response.json()
        })
        .then(data => {
            let picture = document.getElementById("picture_ad")
            let description = document.getElementById("description_ad")
            let wallet = document.getElementById("wallet_id")

            picture.src = data.picture;
            description.innerText = data.description;
            wallet.innerText = data.wallet;
        })
        .catch(err => {
            console.log(err)
        })
}

function registerRequester(email, password)    {
    let requester = {
        "email" : email,
        "password" : password
    }

    fetch(`/register`, {
        method: "POST",
        headers : {
            "Content-Type": "application/json"
        },
        body : JSON.stringify(requester)
    }).then(response => {
        location.href = "home.html"
    })
}