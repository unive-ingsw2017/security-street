using System.Collections.Generic;
using ServiceStack;
using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Services.Models;

namespace SecurityStreet.Server.Services.Autovelox
{
    /// <summary>
    /// Rappresent a read by id request
    /// </summary>
    /// <seealso cref="ServiceStack.IReturn{System.Collections.Generic.List{SecurityStreet.Server.Models.Dto.CrashDto}}" />
    [Route("/crashes/{Id}", "GET")]
    [Route("/crashes", "GET")]
    public class ReadCrashRequest : ReadRequest, IReturn<List<CrashDto>>
    {
    }
}