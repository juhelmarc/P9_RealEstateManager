package com.openclassrooms.realestatemanager.data.property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.denzcoskun.imageslider.models.SlideModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakePropertyRepository @Inject constructor() {

    private val fakeListPropertyLiveData = MutableLiveData<List<Property>>()

    init {

        fakeListPropertyLiveData.value = listOf(
            Property(
                "1",
                "Flat",
                "Manathan",
                1780000,
                "Situation : à Saint Egrève au cœur de la zone commerciale de St Egrève (à 100m de Marie Blachère, restaurant Pacific Grill, Wok d'Asie, Spa Les thermes du Sultan, Biocoop, Mangeons frais, ...) toutes les commodités sont à proximité. La vue est dégagé sur les montagnes notamment sur le Vercors.",
                240,
                8,
                4,
                6,
                "New York",
                "740 Park Avenue",
                "10021",
                "United States",
                "https://img-31.ccm2.net/TRupMSvV5f7ZiaoLWiZoKviphvU=/910x/smart/4a5568d3a6774c24afa624185790edae/ccmcms-hugo/28446566.jpg",
                arrayListOf(SlideModel("https://img.freepik.com/photos-premium/astronaute-dans-espace-exterieur-ouvert-planete-terre-etoiles-fournissent-arriere-plan-formant-espace-au-dessus-planete-terre-lever-du-soleil-coucher-du-soleil-notre-maison-iss-elements-image-fournie-par-nasa_150455-16829.jpg?w=2000","Kitchen" ),
                    SlideModel("https://cdn.futura-sciences.com/buildsv6/images/largeoriginal/5/2/e/52eeace621_89462_son-espace.jpg", "room1"),
                    SlideModel("https://img-31.ccm2.net/TRupMSvV5f7ZiaoLWiZoKviphvU=/910x/smart/4a5568d3a6774c24afa624185790edae/ccmcms-hugo/28446566.jpg","room2"),
                    SlideModel("https://cherry.img.pmdstatic.net/fit/https.3A.2F.2Fimg.2Emaxisciences.2Ecom.2Fs3.2Ffrgsd.2Fastronomie.2Fdefault_2021-10-13_61d236d7-f855-4cbb-98b3-33dfd964c9f1.2Ejpeg/1200x675/quality/80/illustration-de-galaxies-planetes-et-etoiles.jpg","room3"),
                    SlideModel("https://www.tomsguide.fr/content/uploads/sites/2/2021/03/20210312-quasar-espace-docx.jpg","Lounge 1"),
                    SlideModel("https://nomdezeus.fr/wp-content/uploads/2016/09/univers-mort-833x550.jpg","Lounge 2"),
                    SlideModel("https://www.tomsguide.fr/content/uploads/sites/2/2020/08/big-bang-2.jpg","Lounge 3"))
            ),

            Property(
                "2",
                "Flat",
                "Toto",
                6780000,
                "Appartement F5 comprenant 4 belles chambres de + de 11m² situé au 1er étage d'une maison individuelle constitué d'un second logement au rdc.\n" +
                        "Grande pièce à vivre de + de 37m², connecté d'un comptoir \"mange debout\" avec une cuisine américaine équipée d'une plaque de cuisson à induction, four, et hotte.Grand couloir et dégagement de 19m²Terrasse de 11m² avec store." +
                        "Hall d’entrée de 10.67m²" +
                        "Chauffage : poêle à granulés de 11.5KW avec radiateurs à inertie dans la pièce à vivre et radiateurs électrique dans les chambres.",
                240,
                8,
                4,
                6,
                "Meylan",
                "8 avenue des 4 chemins",
                "38240",
                "France",
                "https://sf.sports.fr/wp-content/uploads/2020/09/Nicolas-Batum-37-670x370.jpg",
                arrayListOf(SlideModel("https://www.maisonsclairlogis.fr/wp-content/uploads/maison-contemporaine_onyx-version-nuit.jpg","Kitchen" ),
                    SlideModel("https://i.ytimg.com/vi/AdbkWLZ4CSo/sddefault.jpg", "room1"),
                    SlideModel("https://www.maisonsclairlogis.fr/wp-content/uploads/maison-moderne-villa_version-nuit.jpg","room2"),
                    SlideModel("https://www.depreux-construction.com/wp-content/uploads/2017/07/maison-ultra-moderne-noir-blanc.jpg","room3"),
                    SlideModel("https://www.top-maison.net/wp-content/uploads/2016/09/maison-moderne-10.jpg","Lounge 1"),
                    SlideModel("https://prod-saint-gobain-fr.content.saint-gobain.io/sites/saint-gobain.fr/files/2022-04/maison-contemporaine-la-maison-saint-gobain01.jpg","Lounge 2"),
                    SlideModel("https://www.projets-et-travaux.fr/wp-content/uploads/2020/07/maison-moderne.jpg","Lounge 3"))
            )
        )
    }

    fun getListPropertyLiveData(): MutableLiveData<List<Property>> = fakeListPropertyLiveData

    //Consule la liste de Property et retourne le premier objet de la liste aillant le même id
    fun getPropertyByIdLiveData(id: String): LiveData<Property> = fakeListPropertyLiveData.map {properties ->
        properties.first { it.id == id }
    }

}