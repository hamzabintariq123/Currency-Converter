package com.andela.currencyconverter.data.sections

import com.andela.currencyconverter.data.db.model.ConverterData

open class RecyclerViewItem
class SectionItem(var title: String) : RecyclerViewItem()
class ContentItem(var converterData: ConverterData) : RecyclerViewItem()