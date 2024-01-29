import com.day.cq.replication.Replicator
import com.day.cq.replication.ReplicationActionType
import com.day.cq.replication.ReplicationStatus

def buildComponentsQuery(parent, resourceType) {
    def queryManager = session.workspace.queryManager
    def statement = 'select * from [nt:unstructured] as t where ISDESCENDANTNODE(['+parent+']) AND t.[sling:resourceType] = "' + resourceType + '"'
    queryManager.createQuery(statement, 'JCR-SQL2')
}

def replicatePageIfNotModifiedByContentAuthor(modifiedPage) {
    def replicator = getService(Replicator.class)
    def replicationStatus = replicator.getReplicationStatus(session, modifiedPage.getContentResource().getPath())

    def Calendar pubishedDate = replicationStatus.getLastPublished()
    def Calendar modifiedDate = modifiedPage.getLastModified()

    if (pubishedDate == null || modifiedPage == null) {
        println 'Activated page ' + modifiedPage.getPath()
        activate(modifiedPage.getPath())
        return
    }

    if (modifiedDate.getTime() >= pubishedDate.getTime()) {
        println 'Page was modified after last replication: ' + modifiedPage,getPath()
    }
}

final def String rootPath = '/content/my-project/test'
final def String resourceType = 'my-project/components/inner'
final def String replacementComponentNodeName = 'inner-wrapper'
final def String replacementComponentResourceType = 'my-project/components/wrapper'
final def query = buildComponentsQuery(rootPath, resourceType)
final def result = query.execute()
final def modifiedPages = []

result.nodes.each {
    node ->
        String nodePath = node.path
        String nodeName = node.name

        parentNode = node.getParent()

        if (!parentNode.hasNode(replacementComponentNodeName)) {
            wrapperNode = parentNode.addNode(replacementComponentNodeName, 'nt:unstructured')
            parentNode.orderBefore(replacementComponentNodeName, nodeName)
            wrapperNode.setProperty('sling:resourceType', replacementComponentResourceType)

            session.move(nodePath, wrapperNode.getPath() + '/' + nodeName)
            modifiedPages.add(pageManager.getContainingPage(wrapperNode.getPath()))
            session.save();
        }
}

modifiedPages.each {
    page ->
        replicatePageIfNotModifiedByContentAuthor(page)
}