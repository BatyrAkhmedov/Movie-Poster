package kz.batyr.movieposters.presentation.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.batyr.movieposters.R
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsDbEntity
import kz.batyr.movieposters.data.dataBase_entity.ViewedFilmsFullInfoDnEntity
import kz.batyr.movieposters.data.films_data.FilmFullInfo
import kz.batyr.movieposters.databinding.BottomSheetLayoutBinding
import kz.batyr.movieposters.presentation.MainViewModel
import kz.batyr.movieposters.presentation.recyclerViewAdapters.BottomSheetDialogAdapter

class BottomDialogFragment(
    val filmId: Int,
    private val insert: (String) -> Unit,
    private val onClose: () -> Unit
):BottomSheetDialogFragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var vb:BottomSheetLayoutBinding
    private val adapter = BottomSheetDialogAdapter(
        {collection -> deleteFilm(collection)},
        {collection -> addFilm(collection)},
        lifecycleScope,
        filmId)
    private var film: ViewedFilmsDbEntity? = null
    private var filmFullInfo:ViewedFilmsFullInfoDnEntity? = null
    private var nameNewCollection: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataToAdapter = viewModel.collectionFilmsDB.value.filter { it.collectionName!= getString(R.string.viewed_films) }
        vb.recyclerView.adapter = adapter
        adapter.setData(viewModel.collectionFilmsDB, viewModel.viewedFilmsDB)
        film = viewModel.viewedFilmsDB.value.find { it.id == filmId }
        filmFullInfo = viewModel.viewedFilmsFullInfoDB.value.find { it.id == filmId }
        if (film==null) {
            replaceType(viewModel.getFilmById, "NotAddToCollection")
        }
        film?.let {film ->
            Glide.with(vb.poster.context)
                .load(film.previewPosterUrl)
                .into(vb.poster)
            vb.filmName.text = film.nameRu ?: film.nameEn ?: film.nameOriginal
            vb.yearGenres.text = film.genre
            vb.rate.text= film.rate.toString()

        }
        vb.layoutCreateCollection.setOnClickListener {
            openDialogWindow()
        }




    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onClose()
    }
    fun deleteFilm (collection:String){
        viewModel.deleteFilmFromCollection(collection, film!!.id)

    }
    fun addFilm (collection: String) {
        insert(collection)

        Log.e("TAG", "added? ${createFilmToInsert(filmFullInfo!!, collection)}}")
        Log.e("TAG", "added? ${viewModel.viewedFilmsDB.value.find { it.id == filmId && it.collectionName == collection }}")
    }


    private fun replaceType (beforeReplace:FilmFullInfo, collectionName:String) {
        with (beforeReplace) {
            val rate = ratingImdb ?: ratingKinopoisk
            val rateName = "${ratingKinopoisk ?: ratingImdb ?: 0.0} ${nameRu ?: nameOriginal}"
            val yearGenres = "$year, ${genres?.map { it.genre }?.joinToString(", ")}"
            val ageLimit = if (ratingAgeLimits != null && ratingAgeLimits.length == 5)
                "${ratingAgeLimits.takeLast(2)}+"
            else if (ratingAgeLimits != null && ratingAgeLimits.length == 4)
                "${ratingAgeLimits.takeLast(1)}+"
            else "0+"
            val countryDurationAgeLimit =
                "${countries?.map { it.country }?.joinToString(", ")}, $ageLimit"
            film = ViewedFilmsDbEntity(
                collectionName = collectionName,
                id = id,
                nameRu = nameRu,
                nameEn = nameEn,
                nameOriginal = nameOriginal,
                genre = genres?.first()?.genre ?: "Нет жанра",
                previewPosterUrl = posterUrlPreview,
                rate = rate
            )
            filmFullInfo = ViewedFilmsFullInfoDnEntity(collectionName = collectionName,
                id = id,
                rateName = rateName,
                description = description,
                shortDescription = shortDescription,
                yearGenres = yearGenres,
                countryDurationAgeLimit = countryDurationAgeLimit,
                logoUrl = logoUrl,
                posterUrl = posterUrl,
                nameOriginal = nameOriginal,
                type = type,
                totalEpisodes = totalEpisodes,
                totalSeason = totalSeason)

        }
    }
    private fun createFilmToInsert(film:ViewedFilmsDbEntity, newCollectionName: String): ViewedFilmsDbEntity {
        with (film) {
            return ViewedFilmsDbEntity(
                collectionName = newCollectionName,
                id = id,
                nameRu = nameRu,
                nameEn = nameEn,
                nameOriginal = nameOriginal,
                genre = genre,
                previewPosterUrl = previewPosterUrl,
                rate = rate
            )
        }
    }
    private fun createFilmToInsert(film:ViewedFilmsFullInfoDnEntity, newCollectionName: String): ViewedFilmsFullInfoDnEntity {
        with (film) {
            return ViewedFilmsFullInfoDnEntity(collectionName = newCollectionName,
                id = id,
                rateName = rateName,
                description = description,
                shortDescription = shortDescription,
                yearGenres = yearGenres,
                countryDurationAgeLimit = countryDurationAgeLimit,
                logoUrl = logoUrl,
                posterUrl = posterUrl,
                nameOriginal = nameOriginal,
                type = type,
                totalEpisodes = totalEpisodes,
                totalSeason = totalSeason
            )
        }
    }
    private fun openDialogWindow() {
        val inputLayout = EditText(requireContext())
        inputLayout.inputType = InputType.TYPE_CLASS_TEXT
        val dialog = AlertDialog.Builder(requireContext())
            .setView(inputLayout)
            .setCancelable(false)
            .setTitle("Введите название коллекции")
            .setPositiveButton("Сохранить") { dialog, which ->
                if (inputLayout.text.toString().isNotBlank()) {
                    nameNewCollection = inputLayout.text.toString()
                    viewModel.insertCollection(nameNewCollection!!)
                    Toast.makeText(
                        requireContext(),
                        "Вы создали коллекцию $nameNewCollection",
                        Toast.LENGTH_SHORT
                    )
                    .show()


                    dialog.dismiss()
                } else Toast.makeText(
                    requireContext(),
                    "Напишите название коллекции",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNeutralButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.let { button ->
                button.setBackgroundColor(resources.getColor(R.color.blue, requireContext().theme))
                button.setTextColor(resources.getColor(R.color.white, requireContext().theme))
                button.gravity = Gravity.CENTER
                val layoutParamsPositive = button.layoutParams as LinearLayout.LayoutParams
                layoutParamsPositive.marginEnd = 20
                button.layoutParams = layoutParamsPositive
            }
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.let { button ->
                button.setBackgroundColor(resources.getColor(R.color.blue, requireContext().theme))
                button.setTextColor(resources.getColor(R.color.white, requireContext().theme))
                button.gravity = Gravity.CENTER
                val layoutParamsNegative = button.layoutParams as LinearLayout.LayoutParams
                layoutParamsNegative.marginStart = 20
                button.layoutParams = layoutParamsNegative
            }

            inputLayout.gravity = Gravity.CENTER
            inputLayout.setBackgroundResource(R.color.trans)
        }
        val title = SpannableString("Введите название коллекции")
        title.setSpan(
            TypefaceSpan(resources.getFont(R.font.graphik_regular)),
            0,
            title.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        dialog.setTitle(title)
        dialog.show()
    }
}