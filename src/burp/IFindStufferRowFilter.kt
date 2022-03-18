package burp

interface IFindStufferRowFilter {

    fun rowMeetsCriteria(row : FindStufferRowData) : Boolean

}
