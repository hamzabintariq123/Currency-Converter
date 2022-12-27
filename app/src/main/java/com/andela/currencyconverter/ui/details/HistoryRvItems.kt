package com.andela.currencyconverter.ui.details

import com.andela.currencyconverter.data.db.model.ConverterData

open class HistoryRvItems
class SectionItem(var title: String) : HistoryRvItems()
class ContentItem(var converterData: ConverterData) : HistoryRvItems()