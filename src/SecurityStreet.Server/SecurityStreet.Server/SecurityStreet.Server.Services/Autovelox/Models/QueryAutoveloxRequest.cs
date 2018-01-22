using SecurityStreet.Server.Models.Dto;
using ServiceStack;

namespace SecurityStreet.Server.Services.Autovelox
{
    /// <summary>
    /// Rappesent a query operation of the Autovelox entity
    /// </summary>
    /// <seealso cref="ServiceStack.QueryDb{SecurityStreet.Server.Models.Entities.Autovelox}" />
    /// <seealso cref="ServiceStack.IReturn{ServiceStack.QueryResponse{SecurityStreet.Server.Models.Dto.AutoveloxDto}}" />
    [Route("/query/autovelox", "GET")]
    [Route("/query/autovelox", "POST")]
    public class QueryAutoveloxRequest : QueryDb<Server.Models.Entities.Autovelox>, IReturn<QueryResponse<AutoveloxDto>>
    {
    }
}
