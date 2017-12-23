using SecurityStreet.Server.Models.Dto;
using ServiceStack;
using System.Collections.Generic;

namespace SecurityStreet.Server.Services.Autovelox
{
    [Route("/query/autovelox")]
    public class QueryAutoveloxRequest : QueryDb<Server.Models.Entities.Autovelox>, IReturn<QueryResponse<AutoveloxDto>>
    {
    }
}
