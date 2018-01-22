using SecurityStreet.Server.Models.Dto;
using ServiceStack;

namespace SecurityStreet.Server.Services.Crash
{
    /// <summary>
    /// Rappesent a query operation of the Autovelox entity
    /// </summary>
    /// <seealso cref="ServiceStack.QueryDb{SecurityStreet.Server.Models.Entities.Crash}" />
    /// <seealso cref="ServiceStack.IReturn{ServiceStack.QueryResponse{SecurityStreet.Server.Models.Dto.CrashDto}}" />
    [Route("/query/crashes", "GET")]
    [Route("/query/crashes", "POST")]
    public class QueryCrashRequest : QueryDb<Server.Models.Entities.Crash>, IReturn<QueryResponse<CrashDto>>
    {
    }
}
