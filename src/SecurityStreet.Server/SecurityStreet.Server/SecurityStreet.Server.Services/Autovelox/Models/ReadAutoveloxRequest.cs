﻿using System.Collections.Generic;
using ServiceStack;
using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Services.Models;

namespace SecurityStreet.Server.Services.Autovelox
{
    /// <summary>
    /// Rappresent a read by id request
    /// </summary>
    /// <seealso cref="ServiceStack.IReturn{System.Collections.Generic.List{SecurityStreet.Server.Models.Dto.AutoveloxDto}}" />
    [Route("/autovelox/{Id}", "GET")]
    [Route("/autovelox", "GET")]
    public class ReadAutoveloxRequest : ReadRequest, IReturn<List<AutoveloxDto>>
    {
    }
}